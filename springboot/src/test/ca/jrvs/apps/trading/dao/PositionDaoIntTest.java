package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class PositionDaoIntTest {

  @Autowired
  private PositionDao positionDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private QuoteDao quoteDao;

  private Account account;
  private Trader savedTrader;
  private SecurityOrder securityOrder;
  private Quote savedQuote;
  private Position position;

  @Before
  public void insertOne(){
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("SDG");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);
    savedTrader = new Trader();
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(2020,02,20));
    savedTrader.setEmail("123456@gmail.com");
    savedTrader.setFirstName("Patrick");
    savedTrader.setLastName("Yang");
    savedTrader.setId(traderDao.save(savedTrader).getId());
    account = new Account();
    account.setTrader_id(savedTrader.getId());
    account.setAmount(666d);
    account.setId(accountDao.save(account).getId());
    securityOrder = new SecurityOrder();
    securityOrder.setAccountId(account.getId());
    securityOrder.setPrice(66d);
    securityOrder.setSize(10);
    securityOrder.setStatus("FILLED");
    securityOrder.setTicker(savedQuote.getTicker());
    securityOrder.setNotes("N/a");
    securityOrder.setId(securityOrderDao.save(securityOrder).getId());
    position = new Position();
    position.setAccountId(account.getId());
    position.setTicker(savedQuote.getTicker());
    position.setPosition(securityOrder.getSize());
  }

  @Test
  public void findAllById(){
    List<Position> positions = Lists.newArrayList(
        positionDao.findAllById(
            Arrays.asList(account.getId())
        )
    );
    assertEquals(1, positions.size());
    assertEquals(position.getAccountId(), positions.get(0).getAccountId());
    assertEquals(savedQuote.getTicker(), positions.get(0).getTicker());
    assertEquals(securityOrder.getSize(), positions.get(0).getPosition());
  }

  @After
  public void delete(){
    securityOrderDao.deleteById(securityOrder.getId());
    accountDao.deleteById(account.getId());
    traderDao.deleteById(savedTrader.getId());
  }
}