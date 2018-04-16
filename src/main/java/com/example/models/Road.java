package com.example.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "road")
public class Road {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "state")
	private double state;
	

	public Road() {
		super();
	
	}

	public Road(long road_id, double state ) {
		super();
		this.id = road_id;
		this.state = state;
	}
	
	public long getRoad_id() {
		return id;
	}

	public void setRoad_id(long road_id) {
		this.id = road_id;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}


}
