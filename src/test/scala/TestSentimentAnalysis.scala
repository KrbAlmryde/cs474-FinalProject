/**
  * Created by krbalmryde on 12/10/16.
  */

import org.scalatest._
import com.finalproject.nlp._

class TestSentimentAnalysis extends FunSuite with Matchers {

    test("\"What a wonderful night it is!\" should be positive") {
        SentimentAnalyzer.detect("What a wonderful night it is!")
    }
}
