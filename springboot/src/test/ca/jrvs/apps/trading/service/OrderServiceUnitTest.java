package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.aspectj.weaver.ast.Or;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountDao accountDao;
  @Mock
  private SecurityOrder securityOrder;
  @Mock
  private QuoteDao quoteDao;
  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private OrderService orderService;


}