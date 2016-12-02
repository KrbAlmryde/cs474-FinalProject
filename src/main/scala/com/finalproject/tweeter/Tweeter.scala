package com.finalproject.tweeter

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterFactory, TwitterStream, TwitterStreamFactory}

/**
  * Created by krbalmryde on 12/2/16.
  *
  * Tweeter object used primarily for convenience in generating the various
  * Twitter4j factory objects. Saves me from having to define and declare the
  * OAuth boilerplate
  *
  * Generates a Twitter or TwitterStream object
  */
object Tweeter {

    /**
      * Relevant twitter authetification information.
      * Please dont steal it.
      */

    val consumerKey = "5B73c5z6nLwGtWWTIqdieTAPC"
    val consumerSecret = "LMyBlcTmSdz7iTbcug3A7R5KVQxaCK7PeO2HrkQbFUCriz6Amp"
    val accessToken = "326827611-Ds6Kas4XDgN40FjJvMfHtdIV7tlRrsVPYdzRtfzW"
    val accessTokenSecret = "El9NWfPj2OknPSZRZALMnmhqwDKpemkyfyzf9WSeUgO3Q"

    // Returns a fully qualified TwitterStream object, ready to use and operate
    def Stream: TwitterStream = new TwitterStreamFactory( getConfig.build ).getInstance()

    // Returns a fully qualified Twitter object, ready to use and operate
    def Tweeter: Twitter = new TwitterFactory( getConfig.build ).getInstance()

    // Returns a configuration Builder object. Should be private
    def getConfig:ConfigurationBuilder = {
        new ConfigurationBuilder()
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
    }
}
