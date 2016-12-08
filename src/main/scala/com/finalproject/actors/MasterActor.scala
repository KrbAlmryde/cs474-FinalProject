package com.finalproject.actors

import akka.actor.{Actor, ActorRef, Props}
import com.finalproject.patterns.Messages.{ShutDown, _}
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

    val TrendsPattern = """(t$|trends)(\s+[0-9]+)""".r


    def receive: Receive  = {

        case pattern:String =>
            pattern match {
                case TrendsPattern(_, woeid) =>
                    println("Show me trends")
                    actors("trends") ! Trends(woeid)

                case _ => // User is requesting a pattern
                    println(s"Got a request to search for stuff: ${pattern}")
                    actors("query") ! Query(pattern)
            }
        case Locations(temp) => // The user wants a list of all available locations
            println("Show me locations")
            actors("trends") ! Locations(temp)

        case Empty =>
            println("I want it ALL")
            actors("query") ! Empty

        case ShutDown =>
            actors("query") ! ShutDown
    }
}
