package com.example.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "truck")
public class Truck {
	
	@Id
	private String truck_id;

	@Column(name = "current_speed")
	private int currentSpeed;
	
	@Column(name = "Previous_speed")
	private int previousSpeed;

	@Column(name = "active")
	private boolean active;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="driver_id")
	private Driver driver;

	public Truck() {
		super();
	}

	public Truck(String truck_id, int currentSpeed, int previousSpeed, boolean active, Driver driver) {
		super();
		this.truck_id = truck_id;
		this.currentSpeed = currentSpeed;
		this.previousSpeed = previousSpeed;
		this.active = active;
		this.driver = driver;
	}

	public String getId() {
		return this.truck_id;
	}

	public void setId(String id) {
		this.truck_id = id;
	}


	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public int getPreviousSpeed() {
		return previousSpeed;
	}

	public void setPreviousSpeed(int previousSpeed) {
		this.previousSpeed = previousSpeed;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	
}
