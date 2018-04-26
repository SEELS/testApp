package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.CheckPoints;
import com.example.models.Road;

public interface CheckPointsRepository extends CrudRepository<CheckPoints, Long>  {
	public ArrayList<CheckPoints> findByRoad(Road road); 
	public ArrayList<CheckPoints> findByDeleted(boolean deleted);
}
