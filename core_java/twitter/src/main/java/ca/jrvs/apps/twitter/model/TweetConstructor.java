package ca.jrvs.apps.twitter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TweetConstructor {

  public static Tweet tweetBuild(String text, Double longitude, Double latitude){
    Tweet tweet = new Tweet();
    tweet.setText(text);
    Coordinates coordinates = new Coordinates();
    coordinates.setType("point");
    List<Double> coor = new ArrayList<>();
    coor.add(longitude);
    coor.add(latitude);
    coordinates.setTweetcoordinates(coor);
    tweet.setCoordinates(coordinates);
    return tweet;
  }

  // The following method is in develop for the function of showTweet
  public static Tweet tweetModifier(Tweet tweetOrg, String[] fields){
    String[] availableFields = {"created_at",
        "id",
        "id_str",
        "text",
        "entities",
        "coordinates",
        "retweet_count",
        "favorite_count",
        "favorited",
        "retweeted"};
    Tweet tweet = new Tweet();

    return tweet;
  }
}
