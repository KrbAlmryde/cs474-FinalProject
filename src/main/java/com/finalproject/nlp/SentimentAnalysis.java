package com.finalproject.nlp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


// Deprecated, dont use me
public class SentimentAnalysis {

    void sample(String args[]) throws IOException {
        try {
            int count = 0;
            String tweet;

            ArrayList<String> stopwords = new ArrayList<String>();

            //Reading Stopwords and adding them to a List
            BufferedReader stop = new BufferedReader(new FileReader("src/main/resources/stopwords.txt"));
            String line = "";
            while ((line = stop.readLine()) != null) {
                stopwords.add(line);
            }

            //code to create a hashmap of words and their scores present in AFINN dictionary
            Map<String, String> map = new HashMap<String, String>();
            BufferedReader in = new BufferedReader(new FileReader("src/main/resources/AFINN"));

            line = "";
            while ((line = in.readLine()) != null) {
                String parts[] = line.split("\t");
                map.put(parts[0], parts[1]);
                count++;
            }
            in.close();


            //Reading the tweets from a CSV file
            Scanner inputStream = new Scanner(new FileReader("src/main/resources/TestTweets.csv"));
            while (inputStream.hasNextLine()) {
                float tweetscore = 0;
                tweet = inputStream.nextLine();
                String[] word = tweet.split(" ");


                for (String aWord : word) {
                    //Checking if the word is present in Stopwords; if present, ignoring that word
                    if (!stopwords.contains(aWord.toLowerCase())) {
                        //Checking if the selected word is present in AFINN dictionary or not
                        if (map.get(aWord) != null) {
                            String wordscore = map.get(aWord.toLowerCase());
                            tweetscore = (float) tweetscore + Integer.parseInt(wordscore);
                        }
                    }
                }

                //Storing the tweets with their overall tweet score
                Map<String, Float> sentiment = new HashMap<String, Float>();
                sentiment.put(tweet, tweetscore);
                System.out.println(sentiment.toString());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
}