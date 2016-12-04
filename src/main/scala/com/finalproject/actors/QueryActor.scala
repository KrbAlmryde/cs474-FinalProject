package com.finalproject.actors

import akka.actor.Actor
import com.finalproject.patterns.Messages.{Empty, Query, ShutDown}
import com.finalproject.tweeter.{Tweeter, TweeterStatusListener}
import twitter4j._

import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/3/16.
  */
class QueryActor extends Actor {

    val twitterStream = Tweeter.Stream
        twitterStream.addListener(new TweeterStatusListener)

    def receive: Receive  = {
        case Empty =>
            println(s"${Console.YELLOW}Having a drink from the garden hose...${Console.BOLD}")
            twitterStream.sample("en")

        case Query(pattern) =>
            twitterStream.filter(
                new FilterQuery().track(pattern)
            )

        // This will end the current connection.
        case ShutDown =>
            twitterStream.clearListeners()
            twitterStream.cleanUp()
            twitterStream.shutdown()

            // For convenience we are also restarting so we can keep playing
            twitterStream.addListener(new TweeterStatusListener)
    }
}
