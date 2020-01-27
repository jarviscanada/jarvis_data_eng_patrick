package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private Controller controller;
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
    TwitterDao dao = new TwitterDao(httpHelper);
    TwitterService twitterService = new TwitterService(dao);
    controller = new TwitterController(twitterService);

    // Create a tweet and save it to postTweet which is the tweet that is going to post
    String hashtag = "abc";
    String text1 = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    postTweet = TweetConstructor.tweetBuild(text1, lon, lat);
    String[] args = {"post", postTweet.getText(), "-1:1"};
    tweet = controller.postTweet(args);
  }

  @Test
  public void postTweet() {
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
    String[] args = {"post", tweet.getId_str(), "created_at,id_str"};
    Tweet tweetshow = controller.showTweet(args);
    assertEquals(postTweet.getText(), tweetshow.getText());
    assertNotNull(tweetshow.getCoordinates());
    assertEquals(2, tweetshow.getCoordinates().getTweetcoordinates().size());
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(0),
        tweetshow.getCoordinates().getTweetcoordinates().get(0));
    assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(1),
        tweetshow.getCoordinates().getTweetcoordinates().get(1));
  }

  @Test
  public void deleteTweet() {
    String[] args = {"delete", tweet.getId_str()};
    List<Tweet> tweets= controller.deleteTweet(args);
    for (Tweet tweetobj : tweets){
      assertEquals(postTweet.getText(), tweetobj.getText());
      assertNotNull(tweetobj.getCoordinates());
      assertEquals(2, tweetobj.getCoordinates().getTweetcoordinates().size());
      assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(0),
          tweetobj.getCoordinates().getTweetcoordinates().get(0));
      assertEquals(postTweet.getCoordinates().getTweetcoordinates().get(1),
          tweetobj.getCoordinates().getTweetcoordinates().get(1));
    }
  }
}