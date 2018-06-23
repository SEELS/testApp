package com.example.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "triplocation") 
public class TripLocation {

	public Location getLocation() {
		return location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public TripLocation(Location location, Trip trip) {
		super();
	
		this.location = location;
		this.trip = trip;
	}

	@Id
	private int id;
	
	@ManyToOne
	@JoinColumn(name="location_id")
	private Location location;
	
	@ManyToOne
	@JoinColumn(name="trip_id")
	private Trip trip;
}
