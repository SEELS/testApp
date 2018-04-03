package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Driver;
import com.example.models.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
	public ArrayList<Location> findByDriver(Driver driver);
	
	public Location findFirstByDriverOrderByIdDesc(Driver driver);
}
