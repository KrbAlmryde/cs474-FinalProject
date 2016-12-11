package com.finalproject.actors

import java.net.URI

import akka.actor.Props
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{Cancel, Request}
import com.finalproject.patterns.Messages.{ShutDown, Tweet}
import com.finalproject.Utils.openBrowser


/**
  * Created by krbalmryde on 12/10/16.
  *
  * Tweet Actor behaves as a publisher. Tweets are streamed in
  * through the StatusListener (see com.finalproject.tweeter.TweeterStatusListener)
  * which are then bound to the eventStream of the Actor system, which this
  * actor listens on.
  *
  * Its a bit of a hack, and required digging really deeply into the Akka stream code
  * in order to understand how it worked, but we got it!
  */
object TweetActor {
    def props: Props = Props[TweetActor]
}

class TweetActor extends ActorPublisher[Tweet]{
    import akka.stream.actor.ActorPublisherMessage
    context.system.eventStream.subscribe(self,classOf[Tweet])

    def receive: PartialFunction[Any, Unit] = {
        case s:Tweet => {
            if (isActive && totalDemand > 0) onNext(s)
        }

        case ShutDown => {
            context.stop(self)
            context.system.eventStream.unsubscribe(self)
        }

        case Cancel =>
            println(s"So....Cancel request?")

        case x:Request => {
            openBrowser(new URI("http://localhost:9000/"))  // It works by george it works!
            println(s"I got a request...uhhh what do I do with $x")
        }
    }
}
