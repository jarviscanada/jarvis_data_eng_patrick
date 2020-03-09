package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.Optional;
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
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;

  @Before
  public void insertion() {
    quoteDao = new QuoteDao(new TestConfig().dataSource());
    savedQuote = new Quote();
    insertOne();
  }


  public void insertOne() {
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("SDG");
    savedQuote.setLastPrice(10.1d);
  }

  @Test
  public void findById(){
    String ticker = savedQuote.getId();
    Optional<Quote> quote = quoteDao.findById(ticker);
    assertEquals(quote.get().getAskPrice(), savedQuote.getAskPrice());
    assertEquals(quote.get().getAskSize(), savedQuote.getAskSize());
    assertEquals(quote.get().getBidPrice(), savedQuote.getBidPrice());
    assertEquals(quote.get().getBidSize(), savedQuote.getBidSize());
    assertEquals(quote.get().getId(), savedQuote.getId());
    assertEquals(quote.get().getLastPrice(), savedQuote.getLastPrice());
  }

  @After
  public void deleteOne() {
    quoteDao.deleteById(savedQuote.getId());
  }

}