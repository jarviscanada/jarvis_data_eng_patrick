package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_COLUMN_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource){
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  /**
   * @param quote
   * @return quote
   * @throw DataAccessException for unexpected SQL result or SQL execution failure
   */
  @Override
  public Quote save(Quote quote) {
    if (existsById(quote.getTicker())) {
      int updateRowNo = updateOne(quote);
      if(updateRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    }else {
      addOne(quote);
    }
    return quote;
  }

  /**
   * helper method that saves one quote
   */
  private void addOne(Quote quote) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
    int row = simpleJdbcInsert.execute(parameterSource);
    if (row != 1) {
      throw new IncorrectResultSizeDataAccessException("Failure to insert", 1, row);
    }
  }

  /**
   * helper method that updates one quote
   */
  private int updateOne(Quote quote) {
    String updateSQL = "UPDATE quote SET last_price=?, bid_price=?, bid_size=?, "
        + "ask_price=?, ask_size=? WHERE ticker=?";
    return jdbcTemplate.update(updateSQL, makeUpdateValues(quote));
  }

  /**
   * helper method that make sql update values objects.
   * @param quote to be updated
   * @return UPDATESQL values
   */
  private Object[] makeUpdateValues(Quote quote){
    List<Object> values = new ArrayList<>();
    values.add(quote.getLastPrice());
    values.add(quote.getBidPrice());
    values.add(quote.getBidSize());
    values.add(quote.getAskPrice());
    values.add(quote.getAskSize());
    values.add(quote.getTicker());
    return values.toArray();
  }

  @Override
  public <S extends Quote> Iterable<S> saveAll(Iterable<S> quotes) {
    List<S> result = new ArrayList<>();
    for (Quote quote : quotes) {
      result.add((S) save(quote));
    }
    return result;
  }
  @Override
  public Optional<Quote> findById(String ticker) {
    Optional<Quote> quote = null;
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ticker=? for UPDATE";
    try {
      quote = Optional.ofNullable(jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Quote.class), ticker));
    }catch (EmptyResultDataAccessException e){
      logger.debug("Can't find trader id: " + ticker, e);
    }
    if (quote == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    return quote;
  }

  @Override
  public boolean existsById(String ticker) {
    long count;
    String sql = "SELECT COUNT(*) FROM " + TABLE_NAME +
        " WHERE ticker='" + ticker.toUpperCase() + "'";
    try {
      count = jdbcTemplate.queryForObject(sql, Long.class);
    } catch (NullPointerException e){
      count = 0;
    }
    return count != 0;
  }

  @Override
  public List<Quote> findAll() {
    String sql = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Quote.class));
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not Implemented.");
  }

  @Override
  public long count() {
    long count;
    String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    try {
      count = jdbcTemplate.queryForObject(sql, Long.class);
    } catch (NullPointerException e){
      count = 0;
    }
    return count;
  }

  @Override
  public void deleteById(String ticker) {
    Object args = new Object[] {ticker};
    String sql = "DELETE FROM " + TABLE_NAME + " WHERE ticker=?";
    jdbcTemplate.update(sql, ticker);
  }

  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not Implemented.");
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not Implemented.");
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM " + TABLE_NAME;
    jdbcTemplate.update(sql);
  }
}
