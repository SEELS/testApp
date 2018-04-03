package com.example.Repostitory;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Trip;

public interface TripRepository extends CrudRepository<Trip, Long> {

}
