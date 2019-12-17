package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

public class TwitterHttpHelper implements HttpHelper {

  /**
   * Dependencies are specified as private member variable
   */
  private OAuthConsumer consumer;
  private HttpClient httpClient;

  /**
   * Constructor
   * Setup dependencies using secrets
   *
   * @param consumerKey
   * @param consumerSecret
   * @param accessToken
   * @param tokenSecret
   */
  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken,
      String tokenSecret){
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);
    /**
     * Default = single connection. Discuss source code if time permit
     */
    httpClient = HttpClientBuilder.create().build();
  }

  /**
   * Execute a HTTP Post call
   *
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpPost(URI uri) {
    try {
      return executeHttpRequest(HttpMethod.POST, uri, null);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Failed to execute", e);
    }
  }

  /**
   * Execute a HTTP Get call
   *
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      return executeHttpRequest(HttpMethod.GET, uri, null);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Failed to execute", e);
    }
  }

  /**
   * Helper to execute http request
   *
   * @param method HttpMethod
   * @param uri
   * @param stringEntity
   * @return HttpResponse
   */
  private HttpResponse executeHttpRequest(HttpMethod method, URI uri, StringEntity stringEntity)
      throws IOException, OAuthException {
    if (method == HttpMethod.GET) {
      HttpGet request = new HttpGet(uri);
      consumer.sign(request);
      return httpClient.execute(request);
    } else if (method == HttpMethod.POST) {
      HttpPost request = new HttpPost(uri);
      if (stringEntity != null) {
        request.setEntity(stringEntity);
      }
      consumer.sign(request);
      return httpClient.execute(request);
    } else {
      throw new IllegalArgumentException("Unknown Http method" + method.name());
    }
  }

  public static void main(String[] args) throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    System.out.println(CONSUMER_KEY + "|" + CONSUMER_SECRET + "|" + ACCESS_TOKEN + "|" + TOKEN_SECRET);

    TwitterHttpHelper twitterHttpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    HttpResponse response = twitterHttpHelper.httpPost(new URI("http://api.twitter.com/1.1/statuses/update.json?status=first_tweet2"));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}
