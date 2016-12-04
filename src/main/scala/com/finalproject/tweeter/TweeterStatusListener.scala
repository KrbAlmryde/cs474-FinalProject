package com.finalproject.tweeter

import twitter4j.{StatusListener, _}

/**
  * Created by krbalmryde on 12/3/16.
  */
class TweeterStatusListener extends StatusListener {

    def onStatus(status: Status): Unit = {

        println(s"@${status.getUser.getScreenName}-${status.getText}")

        println(
            s"""
               |@${status.getUser.getScreenName}
               |\t${status.getUser.getName}
               |\t${status.getUser.getId}
               |\t${status.getUser.getLocation}
               |\t${status.getUser.getFollowersCount}
               |\t${status.getUser.getUtcOffset}
               |\t${status.getUser.getTimeZone}
               |\t${status.getUser.getCreatedAt}
               |\t${status.getUser.getLang}
               |\t${status.getCreatedAt}
               |\t${status.getGeoLocation}
               |\t${status.getPlace}
               |\t${status.getText}\n
             """.stripMargin
        )
    }

    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = println(s"Got a status deletion notice id: $statusDeletionNotice")

    def onStallWarning(warning: StallWarning): Unit = println(s"Go stall warning: $warning")

    def onScrubGeo(userId: Long, upToStatusId: Long): Unit = println(s"Got scrub_geo event userID: $userId upToStatusId: $upToStatusId")

    def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = println(s"Got track limitation notice: $numberOfLimitedStatuses")

    def onException(ex: Exception): Unit = ex.printStackTrace()
}
