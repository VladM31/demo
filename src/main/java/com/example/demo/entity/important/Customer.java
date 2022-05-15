package com.example.demo.entity.important;

import com.example.demo.entity.enums.Role;

import java.time.LocalDateTime;

public class Customer extends User{
    private Long customerId;////
    private Boolean male;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean isMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public String getFirstName() {
        return super.getName().substring(0,super.getName().indexOf("/"));
    }

    public void setFirstName(String firstName) {
        super.setName(firstName + super.getName().substring(super.getName().indexOf("/")));
    }

    public String getLastName() {
        return super.getName().substring(super.getName().indexOf("/")+1);
    }

    public void setLastName(String lastName) {
        super.setName(super.getName().substring(0,super.getName().indexOf("/")+1) + lastName);
    }

    public Customer() {
    }

    public Customer(Long id, long number, String email, String username, String password, boolean active,
                    LocalDateTime dateRegistration, Role role, String country, String name, Long customerId, Boolean male) {
        super(id, number, email, username, password, active, dateRegistration, role, country, name);
        this.customerId = customerId;
        this.male = male;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;

        Customer customer = (Customer) o;

        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + customerId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", male=" + male +
                ", firstname = " + this.getFirstName() +
                ", lastname = " + this.getLastName() +
                ", number = " + super.getNumber() +
                ", email = " + super.getEmail() +
                ", username = " + super.getUsername() +
                ", password = " + super.getPassword() +
                ", active = " + super.isActive() +
                ", country = " + super.getCountry() +
                ", date registration = " + super.getDateRegistration() + "};";
    }
}
