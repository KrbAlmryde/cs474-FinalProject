package com.finalproject.tweeter

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterFactory, TwitterStream, TwitterStreamFactory}

/**
  * Created by krbalmryde on 12/2/16.
  */

object TweeterFactory {
    val consumerKey = "5B73c5z6nLwGtWWTIqdieTAPC"
    val consumerSecret = "LMyBlcTmSdz7iTbcug3A7R5KVQxaCK7PeO2HrkQbFUCriz6Amp"
    val accessToken = "326827611-Ds6Kas4XDgN40FjJvMfHtdIV7tlRrsVPYdzRtfzW"
    val accessTokenSecret = "El9NWfPj2OknPSZRZALMnmhqwDKpemkyfyzf9WSeUgO3Q"

    def Stream: TwitterStream = new TwitterStreamFactory(getConfig.build()).getInstance()

    def Tweeter: Twitter = new TwitterFactory(getConfig.build()).getInstance()


    def getConfig:ConfigurationBuilder = {
        new ConfigurationBuilder()
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
    }
}
