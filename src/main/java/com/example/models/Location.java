package com.example.models;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "Lat")
	private Double lat;

	@Column(name = "Lon")
	private Double lon;
	
	@Column(name = "speed")
	private Double speed;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="truck_id")
	private Truck truck;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="driver_id")
	private Driver driver;
	

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="trip_id")
	private Trip trip;
	
	// time stamps
	
	public Location() {
		super();
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	public Truck getTruck() {
		return truck;
	}


	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public Double getLat() {
		return lat;
	}


	public void setLat(Double lat) {
		this.lat = lat;
	}


	public Double getLon() {
		return lon;
	}


	public void setLon(Double lon) {
		this.lon = lon;
	}


	public Driver getDriver() {
		return driver;
	}


	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Double getSpeed() {
		return speed;
	}


	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	
	
	
}
