package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;
  private Quote savedQuote2;

  @Before
  public void setUp() throws Exception {
    savedQuote = new Quote();
    savedQuote2 = new Quote();
    savedQuote2.setAskPrice(10d);
    savedQuote2.setAskSize(10);
    savedQuote2.setBidPrice(10.2d);
    savedQuote2.setBidSize(10);
    savedQuote2.setId("SDG");
    savedQuote2.setLastPrice(10.1d);
    quoteDao.save(savedQuote2);
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
  }

  @Test
  public void findIexQuoteByTicker() {
    IexQuote iexQuote = quoteService.findIexQuoteByTicker("AAPL");
    assertNotNull(iexQuote);
  }

  @Test
  public void updateMarketData() {
    quoteService.updateMarketData();
    assertNotEquals(0, quoteDao.count());
    assertTrue(quoteDao.existsById("SDG"));
    assertFalse(quoteDao.existsById("AAAA"));
  }


  @Test
  public void saveQuote(){
    quoteService.saveQuote(savedQuote);
    assertEquals(2, quoteDao.count());
  }

  @Test
  public void saveQuotes(){
    List<String> tickers = new ArrayList<>();
    tickers.add("JNJ");
    tickers.add("AMZN");
    quoteService.saveQuotes(tickers);
    assertEquals(3, quoteDao.count());
  }


  @Test
  public void findAllQuotes(){
    List<Quote> quotes = quoteService.findAllQuotes();
    assertEquals(1, quoteDao.count());
  }

  @After
  public void cleanup() throws Exception {
    quoteDao.deleteAll();
  }
}