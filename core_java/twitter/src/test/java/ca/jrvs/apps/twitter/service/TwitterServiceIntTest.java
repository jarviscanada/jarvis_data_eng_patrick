package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  TwitterDao dao;
  private TwitterService twitterService;
  private Tweet tweet;
  private Tweet postTweet;

  @Before
  public void setUp() {
    String  consumerKey= System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    dao = new TwitterDao(httpHelper);
    twitterService = new TwitterService(dao);

    // Create a tweet and save it to postTweet which is the tweet that is going to post
    String hashtag = "abc";
    String text1 = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    postTweet = TweetConstructor.tweetBuild(text1, lon, lat);
  }

  @Test
  public void postTweet() {
    tweet = twitterService.postTweet(postTweet);
    assertEquals(postTweet.getText(), tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getTweetcoordinates().size());
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(0),
        tweet.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(1),
        tweet.getCoordinates().getTweetcoordinates().get(1));
  }

  @Test
  public void showTweet() {
    tweet = twitterService.postTweet(postTweet);
    String[] fields = {"created_at", "id_str"};
    Tweet tweetshow = twitterService.showTweet(tweet.getId_str(),fields);
    assertEquals(postTweet.getText(), tweetshow.getText());
    assertNotNull(tweetshow.getCoordinates());
    assertEquals(2,tweetshow.getCoordinates().getTweetcoordinates().size());
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(0),
        tweetshow.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(1),
        tweetshow.getCoordinates().getTweetcoordinates().get(1));
  }

  @Test
  public void deleteTweet(){
    tweet = twitterService.postTweet(postTweet);
    String[] idArray = {tweet.getId_str()};
    List<Tweet> tweetDelete = twitterService.deleteTweets(idArray);
    Tweet tweetshow = tweetDelete.get(0);
    assertEquals(postTweet.getText(), tweetshow.getText());
    assertNotNull(tweetshow.getCoordinates());
    assertEquals(2,tweetshow.getCoordinates().getTweetcoordinates().size());
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(0),
        tweetshow.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(1),
        tweetshow.getCoordinates().getTweetcoordinates().get(1));
  }
}