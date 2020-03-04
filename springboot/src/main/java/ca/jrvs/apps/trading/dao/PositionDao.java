package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao extends JdbcCrudDao<Position> {

  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);

  private final String TABLE_NAME = "position";
  private final String ID_COLUMN = "account_id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public PositionDao(DataSource dataSource){
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
  Class<Position> getEntityClass() {
    return Position.class;
  }

  /**
   * Save an entity and update auto-generated integer ID
   *
   * @param entity
   * @return
   */
  @Override
  public <S extends Position> S save(S entity) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  @Override
  public <S extends Position> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  /**
   * helper method that updates one quote
   *
   * @param entity
   * @return
   */
  @Override
  public int updateOne(Position entity) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  @Override
  public void deleteAll(Iterable<? extends Position> iterable) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  @Override
  public void deleteById(Integer id) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }

  @Override
  public void delete(Position entity) {
    throw new UnsupportedOperationException("This is a read-Only view, you cannot modify it.");
  }
}
