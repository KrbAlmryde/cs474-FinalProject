package com.finalproject

import twitter4j._
import com.finalproject.tweeter.Tweeter
import scala.collection.JavaConversions._

// Twitter Hello
object Main extends App {


    println("Having a drink from the fire hose...")
    val twitterStream = Tweeter.Stream
    twitterStream.addListener(
        new RawStreamListener {
            override def onMessage(rawString: String): Unit = println(s"tweet: $rawString")
            override def onException(ex: Exception): Unit = ex.printStackTrace()
        }
    )
    twitterStream.sample()


/*
    twitterStream.addListener(
        new StatusListener {
            override def onStatus(status: Status): Unit = println(s"@${status.getUser.getScreenName}-${status.getText}")

            override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = println(s"Got a status deletion notice id: $statusDeletionNotice")

            override def onStallWarning(warning: StallWarning): Unit = println(s"Go stall warning: $warning")

            override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = println(s"Got scrub_geo event userID: $userId upToStatusId: $upToStatusId")

            override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = println(s"Got track limitation notice: $numberOfLimitedStatuses")

            override def onException(ex: Exception): Unit = ex.printStackTrace()
        }
    )
    twitterStream.firehose(0)

    ++++++++++++++++++++++++++++++
    val twitter = Tweeter.Tweeter

    try {
        val locations = twitter.getAvailableTrends
        println("Showing available trends")
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

    val rateLimitStatus = twitter.getRateLimitStatus
    val bla = rateLimitStatus.keySet().foreach(key => {
        val status = rateLimitStatus.get(key)
        println(s" Endpoint: $key")
        println(s"    limit: ${status.getLimit}")
        println(s"Remaining: ${status.getRemaining}")
    })
*/
}
