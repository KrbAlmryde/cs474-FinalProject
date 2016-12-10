package com.finalproject.nlp

import Sentiment._

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.JavaConversions._
import scala.io.Source


/**
  * Created by krbalmryde on 12/4/16.
  *
  * Take a look at this one:
  *     https://github.com/vspiewak/twitter-sentiment-analysis/blob/master/src/main/scala/com/github/vspiewak/util/SentimentAnalysisUtils.scala
  *
  */
object SentimentAnalyzer {

    // I *THINK* I can create a single instance...
    val pipeline: StanfordCoreNLP = {
        val props = new Properties()
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
        new StanfordCoreNLP(props)
    }

    def apply(tweet:String):(Emotion, Double) = {
        val annotations = pipeline.process(tweet)
        val sentences = annotations.get(classOf[CoreAnnotations.SentencesAnnotation]).toVector
        val sentiment = sentences.map( sent=> sent.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])).map(tree => RNNCoreAnnotations.getPredictedClass(tree).toDouble)
        val sizes = sentences.map(_.toString.length)

        val maxSentiment:Double = sentiment.max
        val weightedSentiment:Double  = (sentiment, sizes).zipped.map((sent, sz) => sent * sz).sum / sizes.sum
        val averageSentiment:Double = {
            if (sentiment.nonEmpty) sentiment.sum / sentiment.size
            else -1
        }

        // Sentiment(maxSentiment),
        // Sentiment(averageSentiment),

        // Return the result as a tuple
        (classify(weightedSentiment),weightedSentiment)

    }

    def classify(score:Double): Emotion = {
        score match {
            case s if s < -5.0 => UNKNOWN
            case s if s < -4.0 => VERY_NEGATIVE
            case s if s < -2.0 => NEGATIVE
            case s if s >= -2.0 && s <= 2.0 => NEUTRAL
            case s if s > 2.0 => POSITIVE
            case s if s > 3.0 => VERY_POSITIVE
            case s if s > 5.0 => UNKNOWN
        }
    }
}


    /*

    //  import com.finalproject.nlp.SentimentAnalyzer
    //  SentimentAnalyzer.detect()
        SentimentAnalyzer("What a wonderful night it is!")
        SentimentAnalyzer("PokemonGo new update. Wow!! Just loving it.")
        SentimentAnalyzer("What a waste of life of opportunity. He could have made it a lot better.")



        SentimentAnalyzer("this is the worst place I have ever been to")

        SentimentAnalyzer("What a wonderful night it is!")
        SentimentAnalyzer("I'm watching a movie")
        SentimentAnalyzer("It was a nice experience.")
        SentimentAnalyzer("It was a very nice experience.")
        SentimentAnalyzer("I am feeling very sad and frustrated.")
        SentimentAnalyzer("The oil pipeline is being rerouted! Don't ever let them tell you your voice/protest doesn't matter  #NoDAPL #DAPL")
        SentimentAnalyzer("So much respect and honor to/for you and your victory #standingrock")
        SentimentAnalyzer("Absolutely heartbreaking. David Cline, you were so effervescent")
        SentimentAnalyzer("Proof that we the people can defeat tyranny through peaceful resistance. This isn't just a victory-this is everything")
        SentimentAnalyzer("While this fight may not be over, #NoDAPL has shown the power of the People, and the power of love.")
        SentimentAnalyzer("<3 <3 <3 Meanwhile, frustrations are piling up here.")
     */
