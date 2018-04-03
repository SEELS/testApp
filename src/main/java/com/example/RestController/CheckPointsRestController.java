package com.example.RestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.CheckPointsRepository;
import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.GoodRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.RoadRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.CheckPoints;
import com.example.models.Driver;
import com.example.models.Good;
import com.example.models.Location;
import com.example.models.Road;
import com.example.models.Trip;

@RestController
@CrossOrigin(origins = "*")
public class CheckPointsRestController {

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private CheckPointsRepository checkPointsRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private GoodRepository goodRepository;
	@Autowired
	private RoadRepository roadRepository;
	
	
	/* if the manager wants to save the check points of the certain road with locations */
	@RequestMapping(value = "/saveCheckPoints/{trip_id}/{location_id}/{barcode}/{road_id}", method = RequestMethod.GET)
	public boolean saveCheckPoints(@PathVariable long trip_id,@PathVariable long road_id,@PathVariable long location_id,@PathVariable String barcode) 
	{
		Trip trip=tripRepository.findOne(trip_id);
		Location location=locationRepository.findOne(location_id);
		
		Road road;
		if(road_id==0)
		{
			road=null;
		}
		else
		{
			road=roadRepository.findOne(road_id);

		}
		
		Good good;
		if(barcode.isEmpty())
		{
			good=null;
		}
		else
		{
			 good=goodRepository.findOne(barcode);
		}
		CheckPoints checkPoints=new CheckPoints();
		checkPoints.setGood(good);
		checkPoints.setLocation(location);
		checkPoints.setTrip(trip);
		checkPoints.setRoad(road);
		if (checkPointsRepository.save(checkPoints) != null)
			return true;
		return false;
	}
	
	@RequestMapping(value="/getAllCheckPoints",method=RequestMethod.GET)
	public ArrayList<CheckPoints> getAllCheckPoints()
	{
		return (ArrayList<CheckPoints>)checkPointsRepository.findAll();
	}
}
