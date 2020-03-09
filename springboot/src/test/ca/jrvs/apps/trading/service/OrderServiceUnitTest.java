package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountDao accountDao;
  @Mock
  private SecurityOrderDao securityOrderDao;
  @Mock
  private QuoteDao quoteDao;
  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private OrderService orderService;

  @Before
  public void setup(){
    Quote quoteExample = new Quote();
    quoteExample.setBidSize(20);
    quoteExample.setAskSize(20);
    quoteExample.setBidPrice(100d);
    quoteExample.setAskPrice(120d);
    quoteExample.setTicker("JRVS");
    quoteExample.setLastPrice(119d);
    when(quoteDao.findById("JRVS")).thenReturn(Optional.of(quoteExample));
    Account accountExample = new Account();
    accountExample.setAmount(1000d);
    accountExample.setId(10);
    accountExample.setTrader_id(5);
    when(accountDao.findById(any())).thenReturn(Optional.of(accountExample));
    List<Position> positionList = new ArrayList<>();
    Position positionExample = new Position();
    positionExample.setPosition(9);
    positionExample.setTicker("JRVS");
    positionExample.setAccountId(10);
    positionList.add(positionExample);
    when(positionDao.findByOtherId(any(),any())).thenReturn(positionList);
    Account accountOutExample = new Account();
    accountOutExample.setAmount(1000d-600d);
    accountOutExample.setTrader_id(5);
    accountOutExample.setId(10);
    when(accountDao.save(any())).thenReturn(accountOutExample);
    SecurityOrder securityOrderExample = new SecurityOrder();
    securityOrderExample.setStatus("FILLED");
    securityOrderExample.setId(22);
    securityOrderExample.setAccountId(10);
    securityOrderExample.setSize(9);
    securityOrderExample.setTicker("JRVS");
    securityOrderExample.setPrice(120d);
    when(securityOrderDao.save(any())).thenReturn(securityOrderExample);
  }

  @Test
  public void buy(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(10);
    marketOrderDto.setSize(5);
    marketOrderDto.setTicker("JRVS");

    SecurityOrder securityOrderOut = orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());
    assertEquals((Integer) 22, captorSecurityOrder.getValue().getId());
  }

  @Test
  public void sell(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(10);
    marketOrderDto.setSize(-5);
    marketOrderDto.setTicker("JRVS");

    SecurityOrder securityOrderOut = orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());
    assertEquals((Integer) 22, captorSecurityOrder.getValue().getId());
  }
}