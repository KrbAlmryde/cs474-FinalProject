package com.finalproject

import java.util.function.Consumer

import Utils._
import twitter4j._
import com.finalproject.tweeter.TweeterFactory

// Twitter Hello
object Main extends App {
/*
    val twitterStream = TweeterFactory.Stream

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
*/2
    try {
        val twitter = TweeterFactory.Tweeter
        val locations = twitter.getAvailableTrends
        val iterator = locations.iterator()
        println("Showing available trends")
        while (iterator.hasNext){
            val loc = iterator.next
            println(s"${loc.getName} + (woeid: ${loc.getWoeid})")
        }
        println("Done")
    } catch {
        case te: TwitterException =>
            te.printStackTrace()
            println("Failed to get trends")
        case _:Throwable =>
            println("Honestly I dont know")
    }

}
