package ca.jrvs.apps.trading.model.domain;

import java.util.Date;

public class Trader implements Entity<Integer> {

  private String country;
  private String email;
  private Date dob;
  private String firstName;
  private Integer Id;
  private String lastName;

  @Override
  public Integer getId() {
    return this.Id;
  }

  @Override
  public void setId(Integer integer) {
    this.Id = integer;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
