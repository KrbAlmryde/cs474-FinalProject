package controllers


import javax.inject._

import akka.NotUsed
import akka.util._
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.actor.{ActorRef, ActorSystem}


// Play specifics
import play.api.libs.EventSource
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.mvc._

// My library
import actors._
import nlp.SentimentAnalyzer
import patterns.Messages._
import tweeter.TwitterStreamClient
import controllers.openBrowser

import scala.io.StdIn
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

// Twitter Hello
@Singleton
class Application @Inject() (implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) extends Controller {

    var actorRef:ActorRef = ActorRef.noSender
    val twitterStream = new TwitterStreamClient(system)
    SentimentAnalyzer("init") // do this once to initialize the annotator

    // Flow: performs sentiment analysis on tweet, converts Tweet to a EmoTweet, passes it along
    val flowSentiment: Flow[Tweet, EmoTweet, NotUsed] = Flow[Tweet].map[EmoTweet](tweet => {
        val (emotion, score) = SentimentAnalyzer(tweet.body)
        EmoTweet( SentiScore(emotion.toString, score), tweet)
    })

    val flowJson: Flow[EmoTweet, JsValue, NotUsed] = Flow[EmoTweet].map[JsValue](etweet => {
        Json.toJson(etweet)
    })

    // This is
    def index:Action[AnyContent] = Action {
//        Redirect(routes.Application.xxxMyFunctionxxx(List("java", "ruby")))
        Ok(views.html.index(""))
    }

    // This is a http/route method.
    def timeline(query:String): Action[AnyContent] = Action {
        println(s"query? $query")
        twitterStream.listenAndStream(query)
        // There are our blueprints or Graph nodes. They define the steps by which we operate
        val source: Source[Tweet, ActorRef] = Source.actorPublisher[Tweet](TweetActor.props)

        Ok.chunked(source via flowSentiment via flowJson via EventSource.flow)
    }

}



