package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Location;
import com.example.models.Trip;
import com.example.models.TripLocation;

public interface TripLocationRepository extends CrudRepository<TripLocation, Long> {
	
ArrayList <Location> findAllLocationsByTrip(Trip trip);
	
}
