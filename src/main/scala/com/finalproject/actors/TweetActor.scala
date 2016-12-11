package com.finalproject.actors

import akka.actor.Props
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.Request
import com.finalproject.patterns.Messages.{ShutDown, Tweet}

/**
  * Created by krbalmryde on 12/10/16.
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

        case x:Request => {
            println(s"I got a request...uhhh what do I do with $x")
        }
    }
}
