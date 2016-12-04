package com.finalproject.actors

import akka.actor.{Actor, ActorRef, Props}
import com.finalproject.patterns.Messages.{Empty, Locations, Search, Trends}
import org.json4s.DefaultFormats
import twitter4j.FilterQuery

/**
  * Created by krbalmryde on 12/3/16.
  */
class MasterActor extends Actor{
    implicit val formats = DefaultFormats // Brings in default date formats etc.

    // Create and store a Map of our TrendsActor and a Query Actor. We only ever need
    // one of each, and since their internal tweeter objects are relatively expensive
    // to create, better to keep the actors alive and use them when needed.
    val actors:Map[String, ActorRef] = Map[String, ActorRef](
        "trends" -> context.actorOf(Props[TrendsActor], name="trends"),
        "query" -> context.actorOf(Props[QueryActor], name="search")
    )

    def receive: Receive  = {

        case Locations => // The user wants a list of all available locations
            actors("trends") ! Locations

        case trendID:Trends => // The user is requesting a specific list of trending locations
            actors("trends") ! trendID

        case query:Search => // User is requesting a pattern

            actors("query") ! query

        case Empty =>
            actors("query") ! Empty
    }
}
