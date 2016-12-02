/**
 * Created by krbalmryde on 11/30/16.
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * User: mihais
 * Date: 10/2/13
 */
public class Twitter4Food {
    private static final int SLEEP = 500;

    public static void main(String[] args) throws Exception {
//        if(args.length != 2) {
//            System.err.println("Usage: Twitter4Food query|sample <output file>");
//            System.exit(1);
//        }
        final String cmd = "#NothingSaysTheHolidaysLike"; //args[0];
        final String output = "tweets.txt"; //args[1];
        final PrintWriter pw = new PrintWriter(new FileOutputStream(output, true));

        ConfigurationBuilder cb = new ConfigurationBuilder();
        // Set your keys here!
        // For this, you need to register here: https://dev.twitter.com/apps
        // For more, this is a good tutorial on how to use twitter4j: http://bcomposes.wordpress.com/2013/02/26/using-twitter4j-with-scala-to-perform-user-actions/
        cb.setDebugEnabled(false)
                .setOAuthConsumerKey("5B73c5z6nLwGtWWTIqdieTAPC")
                .setOAuthConsumerSecret("LMyBlcTmSdz7iTbcug3A7R5KVQxaCK7PeO2HrkQbFUCriz6Amp")
                .setOAuthAccessToken("326827611-Ds6Kas4XDgN40FjJvMfHtdIV7tlRrsVPYdzRtfzW")
                .setOAuthAccessTokenSecret("El9NWfPj2OknPSZRZALMnmhqwDKpemkyfyzf9WSeUgO3Q");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception x) { x.printStackTrace(); }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) { }

            @Override
            public void onScrubGeo(long arg0, long arg1) { }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.err.println("Received STALL warning: " + warning);
                // System.exit(1);
            }

            @Override
            public void onTrackLimitationNotice(int arg0) { }

            @Override
            public void onStatus(Status status) {
                User u = status.getUser();
                pw.println(
                        "@" + u.getScreenName() + "\t" +
                                c(u.getName()) + "\t" +
                                c(Long.toString(u.getId())) + "\t" +
                                c(u.getLocation()) + "\t" +
                                c(Integer.toString(u.getFollowersCount())) + "\t" +
                                c(Integer.toString(u.getUtcOffset())) + "\t" +
                                c(u.getTimeZone()) + "\t" +
                                c(u.getCreatedAt() != null ? u.getCreatedAt().toString() : null) + "\t" +
                                c(u.getLang()) + "\n" +
                                c(status.getCreatedAt() != null ? status.getCreatedAt().toString() : null) + "\t" +
                                geoLocationToString(status.getGeoLocation()) + "\t" +
                                placeToString(status.getPlace()) + "\n" +
                                c(status.getText()));
                pw.flush();

                if(cmd.equals("sample")) {
                    try {
                        System.err.println("Sleeping for " + (SLEEP/1000.0) + " seconds...");
                        Thread.sleep(SLEEP);
                    } catch (InterruptedException e) {
                        System.err.println("Could not sleep for some reasons...");
                        e.printStackTrace();
                    }
                }
            }

            private String placeToString(Place p) {
                if(p == null) return "NIL";
                StringBuilder os = new StringBuilder();
                os.append(c(p.getPlaceType()))
                  .append("/").append(c(p.getFullName()))
                  .append("/").append(c(p.getCountryCode()))
                  .append("/").append(c(p.getBoundingBoxType()));
                GeoLocation [][] gs = p.getBoundingBoxCoordinates();
                if(gs != null) {
                    for(int i = 0; i < gs.length; i ++) {
                        for(int j = 0; j < gs[i].length; j ++) {
                            os.append("/" + geoLocationToString(gs[i][j]));
                        }
                    }
                }
                return os.toString();
            }

            private String geoLocationToString(GeoLocation g) {
                if(g == null) return "NIL";
                return c(Double.toString(g.getLatitude())) + "|" + c(Double.toString(g.getLongitude()));
            }

            private String c(String s) {
                if(s == null) return "NIL";
                if(s.length() == 0) return "NIL";
                return s.replaceAll("[\\t\\n\\r]+", " ");
            }
        };
        twitterStream.addListener(listener);

        if(cmd.equals("query")) {
            FilterQuery fq = new FilterQuery();
            String keywords[] = { "#dinner", "#lunch", "#breakfast", "#snack", "#brunch", "#supper", "#meal" };
            System.out.println("Following these hashtags:");
            for (String keyword : keywords) System.out.println("\t" + keyword);
            fq.track(keywords);
            twitterStream.filter(fq);
            Thread.sleep(SLEEP * 5);
        } else if(cmd.equals("sample")) {
            twitterStream.sample();
            Thread.sleep(SLEEP * 5);
        }

    }
}
