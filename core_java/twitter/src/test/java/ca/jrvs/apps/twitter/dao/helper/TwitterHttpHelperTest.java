package ca.jrvs.apps.twitter.dao.helper;

import static org.junit.Assert.*;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TwitterHttpHelperTest {

  String CONSUMER_KEY = System.getenv("consumerKey");
  String CONSUMER_SECRET = System.getenv("consumerSecret");
  String ACCESS_TOKEN = System.getenv("accessToken");
  String TOKEN_SECRET = System.getenv("tokenSecret");
  TwitterHttpHelper twitterHttpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
  String text = "";
  int id = 0;

  @Test
  public void httpPost() throws Exception {
    HttpResponse response = twitterHttpHelper.httpPost(new URI("http://api.twitter.com/1.1/statuses/update.json?status=" + text));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }

  @Test
  public void httpGet() throws Exception{
    HttpResponse response = twitterHttpHelper.httpGet(new URI("https://api.twitter.com/1.1/friends/list.json?user_id=" + id));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}
