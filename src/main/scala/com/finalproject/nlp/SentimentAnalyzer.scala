package com.finalproject.nlp

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/4/16.
  *
  * Take a look at this one:
  *     https://github.com/vspiewak/twitter-sentiment-analysis/blob/master/src/main/scala/com/github/vspiewak/util/SentimentAnalysisUtils.scala
  *
  */


sealed trait Emotion
case object Sentiment extends Emotion {
    def apply(score:Int): Unit = {

    }
}
case object POSITIVE extends Emotion
case object NEGATIVE extends Emotion
case object NEUTRAL extends Emotion


object SentimentAnalyzer {


    // I *THINK* I can create a single instance...
    val pipeline: StanfordCoreNLP = {
        val props = new Properties()
            props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
        new StanfordCoreNLP(props)
    }

    def detect(tweet:String):Vector[(String, Int)] = {
        val annotations = pipeline.process(tweet)
            annotations.get(classOf[CoreAnnotations.SentencesAnnotation])
                    .map(sent => (sent, sent.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])))
                    .map { case (sent, tree) => (sent.toString, RNNCoreAnnotations.getPredictedClass(tree))}
                .toVector
    }
}
