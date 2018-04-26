package com.example.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "check_points")
public class CheckPoints {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long check_points_id;
	
	@ManyToOne
	@JoinColumn(name="trip_id")
	private Trip trip;
	
	@ManyToOne
	@JoinColumn(name="road_id")
	private Road road;
	
	@ManyToOne
	@JoinColumn(name="barcode")
	private Good good;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="location_id")
	private Location location;

	@Column(name = "deleted")
	private boolean deleted;
	
	public CheckPoints() {
		super();
	}

	public CheckPoints(long check_points_id, Trip trip,Road road, Good good, Location location) {
		super();
		this.check_points_id = check_points_id;
		this.trip = trip;
		this.road = road;
		this.good = good;
		this.location = location;
	}

	public long getCheck_points_id() {
		return check_points_id;
	}

	public void setCheck_points_id(long check_points_id) {
		this.check_points_id = check_points_id;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	
	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
	
}
