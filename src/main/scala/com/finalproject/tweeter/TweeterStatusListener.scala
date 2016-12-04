package com.finalproject.tweeter

import akka.actor.ActorContext
import twitter4j.{StatusListener, _}

/**
  * Created by krbalmryde on 12/3/16.
  */
class TweeterStatusListener extends StatusListener {

    def onStatus(status: Status): Unit = {

        println(
            s"""
               | @${status.getUser.getScreenName}: ${if(status.isRetweet) status.getRetweetedStatus.getText else status.getText}
               |\t     Name: ${status.getUser.getName}
               |\t       Id: ${status.getUser.getId}
               |\t Location: ${status.getUser.getLocation}
               |\tFollowers: ${status.getUser.getFollowersCount}
               |\tUtcOffset: ${status.getUser.getUtcOffset}
               |\t TimeZone: ${status.getUser.getTimeZone}
               |\tCreatedAt: ${status.getUser.getCreatedAt}
               |\t Language: ${status.getUser.getLang}
               |\tCreatedAt: ${status.getCreatedAt}
               |\t  Lat|Lng: ${geoLocationToString( Option(status.getGeoLocation) )}
               |\t    Place: ${status.getPlace}
             """.stripMargin
        )
    }

    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = println(s"Got a status deletion notice id: $statusDeletionNotice")

    def onStallWarning(warning: StallWarning): Unit = println(s"Go stall warning: $warning")

    def onScrubGeo(userId: Long, upToStatusId: Long): Unit = println(s"Got scrub_geo event userID: $userId upToStatusId: $upToStatusId")

    def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = println(s"Got track limitation notice: $numberOfLimitedStatuses")

    def onException(ex: Exception): Unit = ex.printStackTrace()

    def geoLocationToString(geoLocation: Option[GeoLocation]): String = geoLocation match {
            case Some(x) => s"${x.getLatitude} | ${x.getLongitude}"
            case _ => "NIL"
    }
}
