package com.example.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trip")
public class Trip {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "rate")
	private double rate;
	
	@Column(name = "date")
	private String date;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="driver_id")
	private Driver driver;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="truck_id")
	private Truck truck;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="road_id")
	private Road road;

	@OneToMany(mappedBy="trip",cascade = CascadeType.ALL)
	private Set<Good> goods ;
	

	@ManyToOne
	@JoinColumn(name="source")
	private Location source;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Location destination;
	
	@Column(name = "parent")
	private long parent;


	public Trip() {
		super();
	}

	public Trip(long trip_id, double rate, String date, Driver driver, Truck truck,
			Set<Good> goods, Location source, Location destination, long parent,
			Road road) {
		super();
		this.id = trip_id;
		this.rate = rate;
		this.date = date;
		this.driver = driver;
		this.truck = truck;
		this.goods = goods;
		this.source = source;
		this.destination = destination;
		this.parent = parent;
		this.road=road;
	}



	public Location getSource() {
		return source;
	}

	public void setSource(Location source) {
		this.source = source;
	}



	public Location getDestination() {
		return destination;
	}



	public void setDestination(Location destination) {
		this.destination = destination;
	}



	public long getParent() {
		return parent;
	}



	public void setParent(long parent) {
		this.parent = parent;
	}




	public long getTrip_id() {
		return id;
	}

	public void setTrip_id(long trip_id) {
		this.id = trip_id;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}
	
	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Set<Good> getGoods() {
		return goods;
	}

	public void setGoods(Set<Good> goods) {
		this.goods = goods;
	}

	
	
	
}
