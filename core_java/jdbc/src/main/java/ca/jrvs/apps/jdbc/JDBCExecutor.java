package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JDBCExecutor {

  public static void main (String... args){
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "hplussport", "postgres", "password");
    try{
      Connection connection = dcm.getConnection();
      CustomerDAO customerDAO = new CustomerDAO(connection);
      //create customer example
      Customer customer = new Customer();
      customer.setFirstName("John");
      customer.setLastName("Adams");
      customer.setEmail("jadams.wh.gov");
      customer.setAddress("1234 Main St");
      customer.setCity("Arlington");
      customer.setState("VA");
      customer.setPhone("(555) 555-9845");
      customer.setZipCode("01234");

      Customer dbCustomer = customerDAO.create(customer);
      System.out.println(dbCustomer);
      //find by ID example
      dbCustomer = customerDAO.findById(dbCustomer.getId());
      System.out.println(dbCustomer);
      //update database example
      dbCustomer.setEmail("john.adams@wh.gov");
      dbCustomer = customerDAO.update(dbCustomer);
      System.out.println(dbCustomer);
      //delete data example
      customerDAO.delete(dbCustomer.getId());
      //order and limit example
      customerDAO.findAllSorted(20).forEach(System.out::println);
      //paging example
      System.out.println("Paged");
      for(int i=1;i<3;i++){
        System.out.println("Page number: " + i);
        customerDAO.findAllPaged(10, i).forEach(System.out::println);
      }

      //find order by id example
      OrderDAO orderDAO = new OrderDAO(connection);
      Order order = orderDAO.findById(1000);
      System.out.println(order);
      //get order for customer example
      List<Order> orders = orderDAO.getOrdersForCustomer(789);
      orders.forEach(System.out::println);
    }catch (SQLException e){
      e.printStackTrace();
    }
  }
}