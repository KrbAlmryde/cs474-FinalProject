package com.finalproject.actors

import akka.actor.{Actor, PoisonPill, Props}
import twitter4j.TwitterException
import com.finalproject.patterns.Messages.{Locations, Trends}
import com.finalproject.tweeter.Tweeter
import com.finalproject.Utils.USWOEID

import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/2/16.
  *
  * The TrendsActor handles all requests for Twitter Trends
  * be they the top trending tweets of a particular location
  * or a list of all the available locations from which to
  * request.
  *
  * There should only be 1 of these actors in existence.
  */
class TrendsActor extends Actor {
    val twitter = Tweeter.Tweeter

    def receive: Receive = {

        // Request for all locations
        case Locations => {

            try {
                val locations = twitter.getAvailableTrends
                println("Showing available Locations with trends")
                locations.foreach(loc => {
                    println(s"\t${loc.getName} + (woeid: ${loc.getWoeid})")
                })
                println("Done")

            } catch {
                case te: TwitterException =>
                    te.printStackTrace()
                    println("Failed to get trends")
                case _:Throwable =>
                    println("Honestly I dont know")
            }

        }

        // Receive a Trends request with a parameter
        case Trends(id:String) => {
            val Number = """[0-9]+""".r

            id match {
                case Number(n) =>
                    try {
                        val woeid = n.asInstanceOf[Int]
                        displayLocalTrends(woeid)
                    } catch {
                        case te: TwitterException =>
                            te.printStackTrace()
                            println("Failed to get trends")
                        case _:Throwable =>
                            println("Honestly I dont know")
                    }
                case _ =>
                    try {
                        val locations = twitter.getAvailableTrends
                        val results = locations.filter(loc => {
                            loc.getName.toLowerCase == id.toLowerCase
                        })

                        // If the requested ID or location does not exist, use the USA woeid
                        val woeid = if (results.isEmpty) USWOEID else results.head.getWoeid
                        displayLocalTrends(woeid)

                    } catch {
                        case te: TwitterException =>
                            te.printStackTrace()
                            println("Failed to get trends")
                        case _:Throwable =>
                            println("Honestly I dont know")
                    }
            }
        }

        case _ => println("Im at a loss")
    }

    // Helper method to display the list of local trends for the user
    def displayLocalTrends(woeid:Int):Unit = {
        val trends = twitter.getPlaceTrends(woeid)
        println("Showing available trends")
        trends.getTrends.foreach(t => {
            println(t.getName)
        })
        println("Done")
    }

    def displayRateLimit():Unit = {
        val rateLimitStatus = twitter.getRateLimitStatus
        rateLimitStatus.keySet().foreach(key => {
            val status = rateLimitStatus.get(key)
            println(s" Endpoint: $key")
            println(s"    limit: ${status.getLimit}")
            println(s"Remaining: ${status.getRemaining}")
        })
    }

}
