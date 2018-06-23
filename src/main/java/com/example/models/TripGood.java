package com.example.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trip_good") 
public class TripGood {
	
	@Id
	private long id;
	
	@Column(name = "num_of_goods")
	private int num_of_goods;
	
	@Column(name = "scan_in_num_of_goods")
	private int scan_in_num_of_goods;
	
	@Column(name = "scan_out_num_of_goods")
	private int scan_out_num_of_goods;
	
	@ManyToOne
	@JoinColumn(name="barcode")
	private Good good;
	
	@ManyToOne
	@JoinColumn(name="trip_id")
	private Trip trip;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public int getNum_of_goods() {
		return num_of_goods;
	}

	public void setNum_of_goods(int num_of_goods) {
		this.num_of_goods = num_of_goods;
	}

	public int getScan_in_num_of_goods() {
		return scan_in_num_of_goods;
	}

	public void setScan_in_num_of_goods(int scan_in_num_of_goods) {
		this.scan_in_num_of_goods = scan_in_num_of_goods;
	}
	
	public int getScan_out_num_of_goods() {
		return scan_out_num_of_goods;
	}

	public void setScan_out_num_of_goods(int scan_out_num_of_goods) {
		this.scan_out_num_of_goods = scan_out_num_of_goods;
	}
	
	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public TripGood(int id, Good good, Trip trip, int num_of_goods, int scan_in_num_of_goods , int scan_out_num_of_goods) {
		super();
		this.id = id;
		this.good = good;
		this.trip = trip;
		this.num_of_goods=num_of_goods;
		this.scan_in_num_of_goods=scan_in_num_of_goods;
		this.scan_out_num_of_goods=scan_out_num_of_goods;
	}

	public TripGood() {
		super();
	}
	
	
}