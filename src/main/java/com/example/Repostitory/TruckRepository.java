package com.example.Repostitory;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Driver;
import com.example.models.Truck;

public interface TruckRepository extends CrudRepository<Truck, String>  {
	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	public Truck findByDriver(Driver driver);
}
