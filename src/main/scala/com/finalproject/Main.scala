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
    var actorRef:ActorRef = ActorRef.noSender

    SentimentAnalyzer("init") // do this once to initialize the annotator

    val twitterStream = new TwitterStreamClient(system)


    def makeRequest(query: String): ActorRef ={
        twitterStream.listenAndStream(query)

        // There are our blueprints or Graph nodes. They define the steps by which we operate
        val source: Source[Tweet, ActorRef] = Source.actorPublisher[Tweet](TweetActor.props)

        // Flow: performs sentiment analysis on tweet, converts Tweet to a EmoTweet, passes it along
        val flowSentiment = Flow[Tweet].map[EmoTweet]( tweet => {
            val (emotion, score) = SentimentAnalyzer(tweet.body)
            EmoTweet( SentiScore(emotion, score), tweet)
        })

        // Sink: Its the Final countdown!
        val sink = Sink.foreach[EmoTweet]({
            case EmoTweet(SentiScore(emotion, score), Tweet(author, time, body)) => {
                val tweetResult =
                    s"""
                       |@$author:
                       |\t$time
                       |\t$body
                       |\t$emotion: $score
                     """.stripMargin

                println(tweetResult)
            }
        })

        source.via(flowSentiment).to(sink).run()
    }

    println(instructions)
    while (true) {
        StdIn.readLine(s">: ") match {
            case "q" | "quit" =>
                println(s"Thanks for playing!")
                actorRef ! ShutDown
                twitterStream.stop()

            case "h" | "help" =>
                println(instructions)

            case input: String =>
                println("Down the rabbit hole we go!")
                actorRef = makeRequest(input)

        }
    }

}



