package com.finalproject.nlp

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.Option
import scala.collection.JavaConversions._
import scala.io.Source
/**
  * Created by krbalmryde on 12/4/16.
  *
  * Take a look at this one:
  *     https://github.com/vspiewak/twitter-sentiment-analysis/blob/master/src/main/scala/com/github/vspiewak/util/SentimentAnalysisUtils.scala
  *
  */

sealed trait Emotion

case object Sentiment{

    case object UNKNOWN extends Emotion
    case object VERY_NEGATIVE extends Emotion
    case object NEGATIVE extends Emotion
    case object NEUTRAL extends Emotion
    case object POSITIVE extends Emotion
    case object VERY_POSITIVE extends Emotion

    def apply(score:Double): Emotion = {
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


object SentimentAnalyzer {
    val stopWords:Vector[String] = Source.fromFile("src/main/resources/stopwords.txt").getLines.map( _.replaceAll("""(?m)\s+$""", "") ).toVector
    val wordScores:Map[String, Int] = Source.fromFile("src/main/resources/AFINN").getLines.map( ln => {
        val pair = ln.split("\t")
        (pair(0), pair(1).toInt)
    }).toMap


    def detect(tweet:String):(Emotion, Int) = {
        val result:Int = tweet.split("""\s""").filter( wrd => !stopWords.contains(wrd.toLowerCase)).map({
                    t => wordScores.get(t) match {
                        case Some(num) => num
                        case None => 0
                    }
                })(collection.breakOut).sum

        (Sentiment(result), result)
    }
}

object SentimentAnalyzer2 {


    // I *THINK* I can create a single instance...
    val pipeline: StanfordCoreNLP = {
        val props = new Properties()
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
        new StanfordCoreNLP(props)
    }

    def detect(tweet:String):(Emotion, Double) = {
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

        (
//            Sentiment(maxSentiment),
//            Sentiment(averageSentiment),
            Sentiment(weightedSentiment),
            weightedSentiment
        )
    }
}
    /*

    // import com.finalproject.nlp.SentimentAnalyzer
    // SentimentAnalyzer.detect()
        SentimentAnalyzer.detect("What a wonderful night it is!")
        SentimentAnalyzer.detect("PokemonGo new update. Wow!! Just loving it.")
        SentimentAnalyzer2.detect("PokemonGo new update. Wow!! Just loving it.")
SentimentAnalyzer.detect("What a waste of life of opportunity. He could have made it a lot better.")
SentimentAnalyzer2.detect("What a waste of life of opportunity. He could have made it a lot better.")


        SentimentAnalyzer.detect("this is the worst place I have ever been to")

        SentimentAnalyzer.detect("What a wonderful night it is!")
        SentimentAnalyzer.detect("I'm watching a movie")
        SentimentAnalyzer.detect("It was a nice experience.")
        SentimentAnalyzer.detect("It was a very nice experience.")
        SentimentAnalyzer.detect("I am feeling very sad and frustrated.")
        SentimentAnalyzer.detect("The oil pipeline is being rerouted! Don't ever let them tell you your voice/protest doesn't matter  #NoDAPL #DAPL")
        SentimentAnalyzer.detect("So much respect and honor to/for you and your victory #standingrock")
        SentimentAnalyzer.detect("Absolutely heartbreaking. David Cline, you were so effervescent")
        SentimentAnalyzer.detect("Proof that we the people can defeat tyranny through peaceful resistance. This isn't just a victory-this is everything")
        SentimentAnalyzer.detect("While this fight may not be over, #NoDAPL has shown the power of the People, and the power of love.")
        SentimentAnalyzer.detect("<3 <3 <3 Meanwhile, frustrations are piling up here.")
     */
