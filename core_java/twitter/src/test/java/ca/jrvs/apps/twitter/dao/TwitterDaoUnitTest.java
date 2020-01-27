package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.TweetConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  @Test
  public void postTweet() throws Exception {
    //test failed request
    String hashtag = "#abc";
    String text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try{
      Tweet posttweet = TweetConstructor.tweetBuild(text, lon, lat);
      dao.create(posttweet);
      fail();
    } catch (RuntimeException e){
      assertTrue(true);
    }

    String tweetJsonStr = "{\n"
        + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
        + "   \"id\":1214259303185563648,\n"
        + "   \"id_str\":\"1214259303185563648\",\n"
        + "   \"text\":\"some text here\",\n"
        + "   \"entities\":{\n"
        + "       \"hashtag\":[],\n"
        + "       \"user_mentions\":[]\n"
        + "   },\n"
        + "   \"coordinated\":null,\n"
        + "   \"retweet_count\":0,\n"
        + "   \"favorited_count\":0,\n"
        + "   \"favorited\":false,\n"
        + "   \"retweeted\":false\n"
        + "}";

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any());
    Tweet tweet = spyDao.create(TweetConstructor.tweetBuild(text, lon, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void showTweet() throws Exception {
    //test failed request
    String hashtag = "#abc";
    String text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try{
      dao.findById("1214259303185563648");
    } catch (RuntimeException e){
      assertTrue(true);
    }

    String tweetJsonStr = "{\n"
        + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
        + "   \"id\":1214259303185563648,\n"
        + "   \"id_str\":\"1214259303185563648\",\n"
        + "   \"text\":\"some text here\",\n"
        + "   \"entities\":{\n"
        + "       \"hashtag\":[],\n"
        + "       \"user_mentions\":[]\n"
        + "   },\n"
        + "   \"coordinated\":null,\n"
        + "   \"retweet_count\":0,\n"
        + "   \"favorited_count\":0,\n"
        + "   \"favorited\":false,\n"
        + "   \"retweeted\":false\n"
        + "}";

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any());
    Tweet tweet = spyDao.findById("1214259303185563648");
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  public void deleteTweet() throws Exception {
    //test failed request
    String hashtag = "#abc";
    String text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try{
      dao.deleteById("1214259303185563648");
    } catch (RuntimeException e){
      assertTrue(true);
    }

    String tweetJsonStr = "{\n"
        + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
        + "   \"id\":1214259303185563648,\n"
        + "   \"id_str\":\"1214259303185563648\",\n"
        + "   \"text\":\"some text here\",\n"
        + "   \"entities\":{\n"
        + "       \"hashtag\":[],\n"
        + "       \"user_mentions\":[]\n"
        + "   },\n"
        + "   \"coordinated\":null,\n"
        + "   \"retweet_count\":0,\n"
        + "   \"favorited_count\":0,\n"
        + "   \"favorited\":false,\n"
        + "   \"retweeted\":false\n"
        + "}";

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any());
    Tweet tweet = spyDao.deleteById("1214259303185563648");
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }
}