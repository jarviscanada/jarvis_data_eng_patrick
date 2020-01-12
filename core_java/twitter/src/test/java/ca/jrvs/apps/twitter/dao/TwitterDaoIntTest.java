package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.stream.DoubleStream;
import net.minidev.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  TwitterDao dao;
  private Tweet tweet;
  String text, hashtag;
  Double lat, lon;

  @Before
  public void setUp() throws Exception {
    String  consumerKey= System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    this.dao = new TwitterDao(httpHelper);

    hashtag = "#abc";
    text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    lat = 1d;
    lon = -1d;
    Tweet postTweet = TweetConstructor.tweetBuild(text, lon, lat);

    tweet = dao.create(postTweet);
  }

  @Test
  public void create() throws Exception {
    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getTweetcoordinates().size());
    assertEquals(lon, tweet.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getTweetcoordinates().get(1));

    assertTrue(hashtag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void findById() {
    Tweet tweetshow = dao.findById(tweet.getId_str());
    assertEquals(text, tweetshow.getText());
    assertNotNull(tweetshow.getCoordinates());
    assertEquals(2, tweetshow.getCoordinates().getTweetcoordinates().size());
    assertEquals(lon, tweetshow.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(lat, tweetshow.getCoordinates().getTweetcoordinates().get(1));

    assertTrue(hashtag.contains(tweetshow.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void deleteById() {
    Tweet tweetshow = dao.deleteById(tweet.getId_str());
    assertEquals(text, tweetshow.getText());
    assertNotNull(tweetshow.getCoordinates());
    assertEquals(2, tweetshow.getCoordinates().getTweetcoordinates().size());
    assertEquals(lon, tweetshow.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(lat, tweetshow.getCoordinates().getTweetcoordinates().get(1));

    assertTrue(hashtag.contains(tweetshow.getEntities().getHashtags().get(0).getText()));
  }
}