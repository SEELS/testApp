package com.example.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "driver")
public class Driver {

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long driver_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "ssn")
	private String ssn;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "logged")
	private boolean logged;
	
	@Column(name = "password")
	private String password;
	
	public Driver() {
		super();
	}
	
	public Driver(long driver_id, String name, String ssn, String token, boolean logged, String password) {
		super();
		this.driver_id = driver_id;
		this.name = name;
		this.ssn = ssn;
		this.token=token;
		this.logged=logged;
		this.password=password;
	}
	

	public long getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(long driver_id) {
		this.driver_id = driver_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public void setToken(String token)
	{
		this.token=token;
	}
	
	public void setLogged(boolean logged)
	{
		this.logged=logged;
	}
	
	public String getToken()
	{
		return token;
	}
	public boolean getLogged()
	{
		return logged;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getPassword()
	{
		return password;
	}
	
	
}
