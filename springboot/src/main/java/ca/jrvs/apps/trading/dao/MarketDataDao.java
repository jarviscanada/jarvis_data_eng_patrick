package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * MarketDataDao is respoinsible for getting Quotes from IEX
 */
@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
  private final String IEX_BATCH_URL;

  private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig){
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if(quotes.size() == 0){
      return Optional.empty();
    }else if(quotes.size() == 1){
      iexQuote = Optional.of(quotes.get(0));
    }else{
      throw new DataRetrievalFailureException("Unexpected number of quotes");
    }
    return iexQuote;
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Get quotes from IEX
   * @param tickers is a list of tickers
   * @return a list of IexQuote object
   * @throws IllegalArgumentException if any ticker is invalid or tickers is empty
   * @throws DataRetrievalFailureException if HTTP request failed.
   */
  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) throws IllegalArgumentException, DataRetrievalFailureException{
      int counter = 0;
      for(String ticker: tickers){
        if (!ticker.matches("[a-zA-Z]{2,4}")){
          throw new IllegalArgumentException("invalid ticker");
        }
        counter++;
      }
      if(counter == 0){
        throw new IllegalArgumentException("tickers is empty");
      }
      List<IexQuote> quotes = new ArrayList<>();
      IexQuote quote;
      String url = String.format(IEX_BATCH_URL, String.join(",", tickers));
      Optional<String> quotesString = executeHttpGet(url);
      JSONObject jsonObject = new JSONObject(quotesString.get());
      ObjectMapper mapper = new ObjectMapper();
      for (String ticker : tickers){
        String quoteString = jsonObject.getJSONObject(ticker).getJSONObject("quote").toString();
        System.out.println(quoteString);
        try {
          quote = mapper.readValue(quoteString, IexQuote.class);
        }catch (IOException e){
          throw new RuntimeException("Connot convert JSON to quote object.");
        }
        quotes.add(quote);
      }
      return quotes;
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote iexQuote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Execute a get and return http entity/body as a string
   * @param url of resource
   * @return http response body
   * @throws DataRetrievalFailureException if fail
   */
  private Optional<String> executeHttpGet(String url) throws DataRetrievalFailureException {
    HttpClient httpClient = getHttpClient();
    HttpGet httpRequest = new HttpGet(url);
    HttpResponse httpResponse;
    try {
      httpResponse = httpClient.execute(httpRequest);
    }catch (IOException e){
      throw new RuntimeException("The url is not valid.");
    }
    int status = httpResponse.getStatusLine().getStatusCode();
    if(status != 200){
      throw new DataRetrievalFailureException("Unexpected HTTP status: " + status);
    }
    if (httpResponse.getEntity() == null){
      throw new RuntimeException("Empty response body.");
    }
    String jsonString;
    try{
      HttpEntity httpEntity = httpResponse.getEntity();
      jsonString = EntityUtils.toString(httpEntity);
    }catch (IOException e){
      throw new RuntimeException("Failed to convert from entity to String", e);
    }
    Optional<String> response = Optional.of(jsonString);
    return response;
  }

  /**
   * Borrow a Http client form httpClientConnectionManager
   * @return httpClient
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }
}
