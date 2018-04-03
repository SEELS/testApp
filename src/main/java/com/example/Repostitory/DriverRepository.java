package com.example.Repostitory;


import org.springframework.data.repository.CrudRepository;

import com.example.models.Driver;

public interface DriverRepository extends CrudRepository<Driver, Long> {
	
	
}
