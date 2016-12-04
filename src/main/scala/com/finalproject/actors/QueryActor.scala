package com.finalproject.actors

import akka.actor.Actor
import com.finalproject.patterns.Messages.Empty
import com.finalproject.tweeter.{Tweeter, TweeterStatusListener}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener}

import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/3/16.
  */
class QueryActor extends Actor {

    val twitterStream = Tweeter.Stream
        twitterStream.addListener(new TweeterStatusListener)

    def receive: Receive  = {
        case Empty =>
            println(s"${Console.YELLOW}Having a drink from the fire hose...${Console.BOLD}")
            twitterStream.sample("en")

    }
}
