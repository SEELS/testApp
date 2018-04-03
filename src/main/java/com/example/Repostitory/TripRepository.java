package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Driver;
import com.example.models.Trip;

public interface TripRepository extends CrudRepository<Trip, Long> {

	ArrayList<Trip> findByDriver(Driver driver);

}
