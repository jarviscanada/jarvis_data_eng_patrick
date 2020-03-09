package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import java.util.Date;
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
public class TraderAccountServiceintTest {

  private TraderAccountView savedView;
  @Autowired
  private TraderAccountService traderAccountService;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private AccountDao accountDao;

  @Before
  public void setup() {
    Trader savedTrader = new Trader();
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(2020, 02, 20));
    savedTrader.setEmail("Jarvis_2020@gmail.com");
    savedTrader.setFirstName("Zongpeng");
    savedTrader.setLastName("Yang");
    savedView = traderAccountService.createTraderAndAccount(savedTrader);
    traderAccountService.deposit(savedView.getTrader().getId(), 666d);
  }

  @Test
  public void withdraw() {
    traderAccountService.withdraw(savedView.getTrader().getId(), 333d);
    System.out.println(savedView.getAccount().getAmount());
    traderAccountService.withdraw(savedView.getTrader().getId(), 333d);
  }

  @After
  public void delete() {
    traderAccountService.deleteTraderById(savedView.getTrader().getId());
  }
}