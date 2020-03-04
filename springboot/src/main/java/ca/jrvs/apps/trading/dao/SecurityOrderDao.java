package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);

  private final String TABLE_NAME = "security_order";
  private final String ID_COLUMN = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource){
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return this.simpleInsert;
  }

  @Override
  public String getTableName() {
    return this.TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return this.ID_COLUMN;
  }

  @Override
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

  /**
   * helper method that updates one quote
   *
   * @param securityOrder
   * @return
   */
  @Override
  public int updateOne(SecurityOrder securityOrder) {
    String updateSql = "UPDATE " + getTableName() + " SET account_id=?, notes=?, price=?, size=?,"
        + " status=?, ticker=? WHERE id=?";
    return getJdbcTemplate().update(updateSql, makeUpdateValues(securityOrder));
  }

  /**
   * helper method that make sql update values objects.
   * @param securityOrder to be updated
   * @return UPDATESQL values
   */
  private Object[] makeUpdateValues(SecurityOrder securityOrder){
    List<Object> values = new ArrayList<>();
    values.add(securityOrder.getAccountId());
    values.add(securityOrder.getNotes());
    values.add(securityOrder.getPrice());
    values.add(securityOrder.getSize());
    values.add(securityOrder.getStatus());
    values.add(securityOrder.getTicker());
    values.add(securityOrder.getId());
    return values.toArray();
  }

  @Override
  public void deleteAll(Iterable<? extends SecurityOrder> iterable) {
    throw new UnsupportedOperationException("Not implemented...");
  }
}
