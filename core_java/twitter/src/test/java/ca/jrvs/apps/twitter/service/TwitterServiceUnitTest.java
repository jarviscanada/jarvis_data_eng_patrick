package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService twitterService;

  @Test
  public void postTweet() {
    Tweet tweet = new Tweet();
    when(dao.create(any())).thenReturn(tweet);
    tweet = twitterService.postTweet(TweetConstructor.tweetBuild("sometext", 1.0, 1.0));
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
    String[] fields = {"created_at", "id_str"};
    when(dao.findById(any())).thenReturn(tweet);
    tweet = twitterService.showTweet(id, fields);
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
    String[] id = {"1111111111111111111", "0000000000000000000"};
    when(dao.deleteById(any())).thenReturn(tweet);
    List<Tweet> tweets = twitterService.deleteTweets(id);
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