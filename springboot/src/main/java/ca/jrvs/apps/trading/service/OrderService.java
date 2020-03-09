package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private QuoteDao quoteDao;
  private PositionDao positionDao;

  @Autowired
  public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
      QuoteDao quoteDao, PositionDao positionDao){
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }

  /**
   * Execute a market order
   *
   * - validate the order(e.g. size, and table)
   * - Create a securityOrder (for security_order table)
   * - Handle buy or sell order
   *  - buy order : check account balance (calls helper method)
   *  - sell order : check position for the ticker/symbol (calls helper method)
   *  - (please dont forget to update securityOrder.status)
   * - Save and return securityOrder
   *
   * @param orderDto market order
   * @return SecurityOrder from security_order Table
   * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
   * @throws IllegalArgumentException for invalid input
   */
  public SecurityOrder executeMarketOrder (MarketOrderDto orderDto) {
    String ticker = orderDto.getTicker().toUpperCase();
    Integer accountId = orderDto.getAccountId();
    Quote quote = quoteDao.findById(ticker).get();
    Account account = accountDao.findById(accountId).get();

    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setSize(orderDto.getSize());
    securityOrder.setTicker(orderDto.getTicker());
    securityOrder.setAccountId(accountId);
    securityOrder.setStatus("CREATED");
    if(orderDto.getSize() > 0){
      if(orderDto.getSize() > quote.getAskSize()){
        throw new IllegalArgumentException("Size is too large");
      }
      securityOrder.setPrice(quote.getAskPrice());
      handleBuyMarketOrder(orderDto, securityOrder, account);
      quote.setAskSize(quote.getAskSize() - orderDto.getSize());
    }else if (orderDto.getSize() < 0) {
      if(orderDto.getSize() > quote.getAskSize()){
        throw new IllegalArgumentException("Size is too large");
      }
      securityOrder.setPrice(quote.getBidPrice());
      handleSellMarketOrder(orderDto, securityOrder, account);
      quote.setBidPrice(quote.getBidPrice() - orderDto.getSize());
    }
    securityOrder.setStatus("FILLED");
    return securityOrder;
  }

  /**
   * Helper method that execute a buy order
   * @param marketOrderDto user order
   * @param securityOrder to be saved in database
   * @param account account
   */
  protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account) {
    if (securityOrder.getPrice()*securityOrder.getSize() > account.getAmount()) {
      throw new IllegalArgumentException("Insufficient amount in the account");
    }
    Double newamount = account.getAmount() - securityOrder.getPrice()*securityOrder.getSize();
    account.setAmount(newamount);
    accountDao.save(account);
    securityOrder.setStatus("FILLED");
    securityOrder.setId(securityOrderDao.save(securityOrder).getId());
  }

  /**
   * Helper method that execute a sell order
   * @param marketOrderDto user order
   * @param securityOrder to be saved in database
   * @param account account
   */
  protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account) {
    List<Position> positions = positionDao.findByOtherId(account.getId(), "account_id");
    if (positions == null){
      throw new IllegalArgumentException("The order size is larger than the security position.");
    }
    boolean found = false;
    for (Position p : positions){
      if (p.getTicker() == marketOrderDto.getTicker().toUpperCase() && p.getPosition() < marketOrderDto.getSize()){
        found = true;
      }
    }
    if (!found){
      throw new IllegalArgumentException("You do not own position for security");
    }
    Double newprice = account.getAmount() + securityOrder.getPrice() * securityOrder.getSize() * -1d;
    account.setAmount(newprice);
    accountDao.save(account);
    securityOrder.setStatus("FILLED");
    securityOrder.setId(securityOrderDao.save(securityOrder).getId());
  }

}
