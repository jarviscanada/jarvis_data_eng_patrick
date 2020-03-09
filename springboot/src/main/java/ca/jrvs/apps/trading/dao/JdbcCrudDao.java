package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  abstract public JdbcTemplate getJdbcTemplate();

  abstract public SimpleJdbcInsert getSimpleJdbcInsert();

  abstract public String getTableName();

  abstract public String getIdColumnName();

  abstract Class<T> getEntityClass();

  /**
   * Save an entity and update auto-generated integer ID
   * @param entity
   * @param <S>
   * @return
   */
  @Override
  public <S extends T> S save(S entity) {
    if (existsById(entity.getId())) {
      if (updateOne(entity) != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else {
      addOne(entity);
    }
    return entity;
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
    List<S> out = new ArrayList<>();
    for (S q : entities) {
      out.add((S) save(q));
    }
    return out;
  }

  /**
   * helper method that saves one quote
   * @param entity
   * @param <S>
   */
  private <S extends T> void addOne(S entity){
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
    Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
    entity.setId(newId.intValue());
  }

  /**
   * helper method that updates one quote
   * @param entity
   * @return
   */
  abstract public int updateOne(T entity);

  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";
    try {
      entity = Optional.ofNullable((T) getJdbcTemplate().queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()), id));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find trader id:" + id, e);
    }
    return entity;
  }

  public List<T> findByOtherId(Integer id, String column){
    List<T> entity = new ArrayList<>();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + column + "=?";
    try {
      entity = getJdbcTemplate().
          query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()), id);
    } catch (IncorrectResultSizeDataAccessException e ){
      logger.debug("Can't find column with id: " + id, e);
    }
    return entity;
  }

  @Override
  public boolean existsById(Integer id) {
    boolean exist = false;
    long count;
    String existSql = "SELECT COUNT(*) FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";
    try {
      count = getJdbcTemplate().queryForObject(existSql, Long.class, id);
      exist = count != 0;
    } catch (NullPointerException e){
      exist = false;
    }
    return exist;
  }

  @Override
  public List<T> findAll() {
    String selectSql = "SELECT * FROM " + getTableName();
    return getJdbcTemplate().query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
  }

  @Override
  public List<T> findAllById(Iterable<Integer> ids) {
    List<T> entities = new ArrayList<>();
    for (Integer id : ids) {
      entities.add(findById(id).get());
    }
    return entities;
  }

  @Override
  public long count() {
    String countSql = "SELECT COUNT(*) FROM " + getTableName();
    long count;
    try {
      count = getJdbcTemplate().queryForObject(countSql, Long.class);
    } catch (NullPointerException e) {
      count = 0;
    }
    return count;
  }

  @Override
  public void deleteById(Integer id) {
    String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";
    getJdbcTemplate().update(deleteSql, id);
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + getTableName();
    getJdbcTemplate().update(deleteSql);
  }

  @Override
  public void delete(T entity){
    deleteById(entity.getId());
  }
}
