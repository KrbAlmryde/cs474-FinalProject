package com.finalproject

import com.finalproject.Utils._
import com.finalproject.actors._
import com.finalproject.patterns.Messages._
import com.finalproject.nlp.SentimentAnalyzer
import java.net.URI

import akka.NotUsed

import scala.io.StdIn
import akka.stream._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.javadsl.Keep
import akka.stream.scaladsl.{Broadcast, Flow, Sink, Source}
import com.finalproject.tweeter.TwitterStreamClient


// Twitter Hello
object Main extends App {

    implicit val system = ActorSystem("FinalProject")
    implicit val materializer = ActorMaterializer()
    SentimentAnalyzer("init") // do this once to initialize the annotator

    val twitterStream = new TwitterStreamClient(system)
    twitterStream.listenAndStream("Happy Holidays")

    /*
        OOOOOHHHHH!!
        Sources
        Broacasts
        Flows
        Sinks
        etc

        Are BLUEPRINTS. That is, they merely define the operations, they dont contain any data.
        Just the instructions by which to operate on!
     */
    // There are our blueprints or Graph nodes. They define the steps by which we operate
    val source: Source[Tweet, ActorRef] = Source.actorPublisher[Tweet](TweetActor.props)

    // Flow: performs sentiment analysis on tweet, converts Tweet to a EmoTweet, passes it along
    val flowSentiment = Flow[Tweet].map[EmoTweet]( tweet => {
        val (emotion, score) = SentimentAnalyzer(tweet.body)
        EmoTweet( SentiScore(emotion, score), tweet)
    })

    // Sink: Its the Final countdown!
    val sink = Sink.foreach[EmoTweet]({
        case EmoTweet(SentiScore(emotion, score), Tweet(author, _, body)) => {
            println(s"@$author: $body\n$emotion:$score")
        }
    })

    source
        .via(flowSentiment)
        .to(sink)
        .run()

    //    openBrowser(new URI("http://localhost:9000/"))  // It works by george it works!
}



