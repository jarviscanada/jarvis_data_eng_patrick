package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DataAccessObject<Customer> {
  //SQL statement for functions
  private static final String INSERT = "INSERT INTO customer (first_name, last_name," +
      "email, phone, address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ONE = "SELECT customer_id, first_name, last_name, " +
      "email, phone, address, city, state, zipcode FROM customer WHERE customer_id=?";
  private static final String UPDATE = "UPDATE customer SET first_name = ?, last_name=?, " +
      "email = ?, phone = ?, address = ?, city = ?, state = ?, zipcode = ? WHERE customer_id = ?";
  private static final String DELETE = "DELETE FROM customer WHERE customer_id = ?";
  private static final String GET_ALL_LMT = "SELECT customer_id, first_name, last_name, email, phone, " +
      "address, city, state, zipcode FROM customer ORDER BY last_name, first_name LIMIT ?";
  private static final String GET_ALL_PAGED = "SELECT customer_id, first_name, last_name, email, phone, " +
      "address, city, state, zipcode FROM customer ORDER BY last_name, first_name LIMIT ? OFFSET ?";

  //Start the connection
  public CustomerDAO(Connection connection) {
    super(connection);
  }

  @Override
  //find customer by ID.
  public Customer findById(long id) {
    Customer customer = new Customer();
    try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipcode"));
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customer;
  }

  @Override
  public List<Customer> findAll() {
    return null;
  }

  @Override
  //update customer information
  public Customer update(Customer dto) {
    Customer customer = null;
    try{
      this.connection.setAutoCommit(false);
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.setLong(9, dto.getId());
      statement.execute();
      this.connection.commit();
      customer = this.findById(dto.getId());
    }catch(SQLException e){
      try{
        this.connection.rollback();
      }catch (SQLException sqle){
        e.printStackTrace();
        throw new RuntimeException(sqle);
      }
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customer;
  }

  @Override
  //create new customer and insert into database
  public Customer create(Customer dto) {
    try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.execute();
      int id = this.getLastVal(CUSTOMER_SEQUENCE);
      return this.findById(id);
    }catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  //return limit customer in order
  public List<Customer> findAllSorted(int limit){
    List<Customer> customers = new ArrayList<>();
    try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_LMT);){
      statement.setInt(1, limit);
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipcode"));
        customers.add(customer);
      }
    }catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customers;
  }
  //return limit customer in order by page.
  public List<Customer> findAllPaged(int limit, int pageNumber){
    List<Customer> customers = new ArrayList<>();
    int offset = ((pageNumber-1) * limit);
    try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);){
      if(limit<1){
        limit=10;
      }
      statement.setInt(1, limit);
      statement.setInt(2, offset);
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipcode"));
        customers.add(customer);
      }
    }catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customers;
  }

  @Override
  //delete a customer
  public void delete(long id) {
    try (PreparedStatement statement = this.connection.prepareStatement(DELETE);) {
      statement.setLong(1, id);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}