package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import java.util.List;
import ca.jrvs.apps.twitter.service.Service;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service serivce;

  @AutoWired
  public TwitterController(Service service) { this.serivce = service; }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "Error: number of argument is not correct. USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longtitude\""
      );
    }

    String text = args[1];
    if(StringUtils.isEmpty(text)){
      throw  new IllegalArgumentException(
          "Error: Tweet text should not be empty"
      );
    }
    String[] coordinates = args[2].split(COORD_SEP);
    if (coordinates.length != 2){
      throw new IllegalArgumentException(
          "Error: The format of Coordinates is not correct. Fomate: longitude:latitude."
      );
    }
    Double lon = Double.parseDouble(coordinates[0]);
    Double lat = Double.parseDouble(coordinates[1]);

    Tweet tweet = TweetConstructor.tweetBuild(text, lon, lat);
    return serivce.postTweet(tweet);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "Error: number of argument is not correct. USAGE: TwitterCLIApp show \"tweet id\", \"field,field,...\""
      );
    }
    String id = args[1];
    if(!id.matches("[0-9]+")){
      throw new IllegalArgumentException(
          "Error: id should not contain characters"
      );
    }
    String[] fields = args[2].split(COMMA);
    if (fields.length > 11){
      throw new IllegalArgumentException(
          "Error: There are too many fields. Format: field,field,..."
      );
    }

    return serivce.showTweet(id, fields);
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Error: number of argument is not correct. USAGE: TwitterCLIApp show \"tweet_id,tweet_id,...\""
      );
    }
    String[] ids = args[1].split(COMMA);
    for(String id : ids) {
      if (!id.matches("[0-9]+")) {
        throw new IllegalArgumentException(
            "Error: id should not contain characters"
        );
      }
    }
    return serivce.deleteTweets(ids);
  }
}
