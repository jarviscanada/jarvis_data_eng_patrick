package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service {

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao) {this.dao = dao;}

  @Override
  public Tweet postTweet(Tweet tweet) {
    //Business logic:
    //e.g. text length, lat/lon range, id format
    validatePostTweet(tweet);

    //create tweet via dao
    return (Tweet) dao.create(tweet);
  }

  /**
   * Search a tweet by ID
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {
    String[] correctfields = {"created_at", "id", "id_str", "text", "source",
        "coordinates", "entities", "retweet_count", "favoriated_count", "favoriated", "retweeted"};
    validateId(id);
    for (String field: fields){
      if (!Arrays.asList(correctfields).contains(field)){
        throw new RuntimeException("You have entered incorrect fields, please correct all the field you input");
      }
    }
    Tweet tweet = (Tweet) dao.findById(id);
    return tweet;
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List <Tweet> tweets = new ArrayList<>();

    for (String id : ids){
      validateId(id);
      tweets.add((Tweet) dao.deleteById(id));
    }
    return tweets;
  }

  private void validatePostTweet(Tweet tweet){
    if (tweet.getText().length() > 140) {
      throw new RuntimeException("You have exceeds the words limit, please lower it under 140 words");
    }
    Double longitude = tweet.getCoordinates().getTweetcoordinates().get(0);
    Double latitude = tweet.getCoordinates().getTweetcoordinates().get(1);
    if (longitude > 180 || longitude < -180) {
      throw new RuntimeException("You entered a incorrect longitude, please enter a correct one");
    }
    if (latitude > 90 || latitude < -90) {
      throw new RuntimeException("You entered a incorrect latitude, please enter a correct one");
    }
  }

  private void validateId(String id) {
    if (id.length() != 19){
      throw new RuntimeException("The id ecceeded the words limit, it should under 19 characters.");
    }
  }

}
