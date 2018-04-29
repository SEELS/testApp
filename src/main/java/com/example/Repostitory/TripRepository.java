package com.example.Repostitory;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Driver;
import com.example.models.Trip;
import com.example.models.Truck;

public interface TripRepository extends CrudRepository<Trip, Long> {

	public ArrayList<Trip> findByDriver(Driver driver);
	public Trip findFirstByTruckOrderByIdDesc(Truck truck);
	public ArrayList<Trip> findByDriverAndDeletedAndDateGreaterThanEqual(Driver driver,boolean deleted,Date date);

}
