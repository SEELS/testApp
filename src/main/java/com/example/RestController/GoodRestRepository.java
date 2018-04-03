package com.example.RestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.GoodRepository;
import com.example.Repostitory.TripRepository;
import com.example.models.Truck;

@RestController
@CrossOrigin(origins = "*")
public class GoodRestRepository {
	
	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private TripRepository tripRepository;
	


}
