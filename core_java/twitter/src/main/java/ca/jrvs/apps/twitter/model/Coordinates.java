package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coordinates",
    "type"
})
public class Coordinates {
  @JsonProperty("coordinates")
  private List<Double> tweetcoordinates;
  @JsonProperty("type")
  private String type;

  public List<Double> getTweetcoordinates() {
    return tweetcoordinates;
  }

  public void setTweetcoordinates(List<Double> tweetcoordinates) {
    this.tweetcoordinates = tweetcoordinates;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
