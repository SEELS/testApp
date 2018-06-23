package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Good;
import com.example.models.Trip;
import com.example.models.TripGood;

public interface TripGoodRepository extends CrudRepository<TripGood, Long>
{

	ArrayList<Good>  findAllGoodsByTrip(Trip trip);
	ArrayList<Trip> findAllTripsByGood(Good good);
	ArrayList<TripGood> findAllByGood(Good good);
	TripGood findByTripAndGood(Trip trip, Good good);
}
