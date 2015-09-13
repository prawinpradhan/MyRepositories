package com.demo.model;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;

public class Account {
	
	@Size(min=2,max=20)
	private String firstName;
	
	@Size(min=2,max=20)
	private String lastName;
	
	@NotNull @NumberFormat @Min(15)
	private int age = 15;
	
	@Size(min=10, max=100)
	private String address;
	
	@Email
	private String email;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
