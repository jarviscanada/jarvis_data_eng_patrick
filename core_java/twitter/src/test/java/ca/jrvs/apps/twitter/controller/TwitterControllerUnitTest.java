package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  TwitterService twitterService;

  @InjectMocks
  TwitterController twitterController;

  @Test
  public void postTweet() {
    Tweet tweet = new Tweet();
    when(twitterService.postTweet(any())).thenReturn(tweet);
    String[] args = {"post", "sometext", "1:1"};
    tweet = twitterController.postTweet(args);
    assertNull(tweet.getText());
    assertNull(tweet.getCreatedTime());
    assertNull(tweet.getEntities());
    assertNull(tweet.getCoordinates());
    assertNull(tweet.getId_str());
    assertNull(tweet.getFavorited());
    assertNull(tweet.getRetweeted());
  }

  @Test
  public void showTweet() {
    Tweet tweet = new Tweet();
    String id = "1111111111111111111";
    when(twitterService.showTweet(any(), any())).thenReturn(tweet);
    String[] args = {"show" , id, "created_at,id_str"};
    tweet = twitterController.showTweet(args);
    assertNull(tweet.getText());
    assertNull(tweet.getCreatedTime());
    assertNull(tweet.getEntities());
    assertNull(tweet.getCoordinates());
    assertNull(tweet.getId_str());
    assertNull(tweet.getFavorited());
    assertNull(tweet.getRetweeted());
  }

  @Test
  public void deleteTweet() {
    Tweet tweet = new Tweet();
    List<Tweet> tweetsOrg = new ArrayList<>();
    when(twitterService.deleteTweets(any())).thenReturn(tweetsOrg);
    String[] args = {"delete", "1111111111111111111,0000000000000000000"};
    List<Tweet> tweets = twitterController.deleteTweet(args);
    for(Tweet tweettemp : tweets){
      assertNull(tweettemp.getText());
      assertNull(tweettemp.getCreatedTime());
      assertNull(tweettemp.getEntities());
      assertNull(tweettemp.getCoordinates());
      assertNull(tweettemp.getId_str());
      assertNull(tweettemp.getFavorited());
      assertNull(tweettemp.getRetweeted());
    }
  }

}