package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  //URI constant
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   *
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    URI uri;
    try{
      uri = getPostUri(entity);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execite Http request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deser response to tweet object
    return parseResponseBody(response);
  }

  /**
   * Find an entity(Tweet) by its id
   *
   * @param s entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(String s) {
    URI uri;
    try{
      uri = getGetUri(s);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid id input", e);
    }

    HttpResponse response = httpHelper.httpGet(uri);

    return parseResponseBody(response);
  }

  /**
   * Delete an entity(Tweet) by its ID
   *
   * @param s of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(String s) {
    URI uri;
    try {
      uri = getDeleteUri(s);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(" Invalid tweet input", e);
    }

    HttpResponse response = httpHelper.httpPost(uri);

    return parseResponseBody(response);
  }

  /**
   * Check response status code Convert Response Entity to Tweet
   *
   * @param response HttpResponse
   * @return Tweet
   */
  Tweet parseResponseBody(HttpResponse response) {
    Tweet tweet = null;

    int status = response.getStatusLine().getStatusCode();
    if (status != TwitterDao.HTTP_OK) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected Http status:" + status);
    }

    if (response.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    String jsonStr;
    try{
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    try {
      System.out.println(jsonStr);
      tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to convert JSON str to Object", e);
    }

    return tweet;
  }

  //get Post method URI
  private URI getPostUri(Tweet tweet) throws URISyntaxException {
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    return new URI(API_BASE_URI + POST_PATH + QUERY_SYM
        + "status" + EQUAL + percentEscaper.escape(tweet.getText())
        + AMPERSAND + "long" + EQUAL + tweet.getCoordinates().getTweetcoordinates().get(0)
        + AMPERSAND + "lat" + EQUAL + tweet.getCoordinates().getTweetcoordinates().get(1));
  }

  //get Get method URI
  private URI getGetUri(String string) throws URISyntaxException {
    return new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + string);
  }

  //get Delete method URI
  private URI getDeleteUri(String string) throws URISyntaxException {
    return new URI(API_BASE_URI + DELETE_PATH + "/" + string + ".json");
  }
}
