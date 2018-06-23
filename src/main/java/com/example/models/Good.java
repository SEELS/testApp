package com.example.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name =  "good")
public class Good {
	
	@Id
	private String barcode;
		
	@Column(name = "name")
	private String name ;
	
	@Column(name = "company")
	private String company ;
	
	@Column(name = "date")
	private Date date ;
	
	@Column(name = "state")
	private int state;
	
	@Column(name = "num_of_goods")
	private int num_of_goods;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@OneToMany(mappedBy="good",cascade = CascadeType.ALL)
	private Set<TripGood> goods ;

	public Good() {
		super();
	}

	

	public Set<TripGood> getTripGood() {
		return goods;
	}



	public void setTripGood(Set<TripGood> goods) {
		this.goods = goods;
	}



	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date startDate) {
		this.date = startDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getNum_of_goods() {
		return num_of_goods;
	}

	public void setNum_of_goods(int num_of_goods) {
		this.num_of_goods = num_of_goods;
	}



	public boolean isDeleted() {
		return deleted;
	}



	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public void setId(String new_barcode) {
		// TODO Auto-generated method stub
		this.barcode=new_barcode;
	}
	
	

	
	
	

}
