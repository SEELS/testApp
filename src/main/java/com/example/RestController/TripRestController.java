package com.example.RestController;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.CheckPointsRepository;
import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.RoadRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.CheckPoints;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Road;
import com.example.models.Trip;
import com.example.models.Truck;


@RestController
@CrossOrigin(origins = "*")
public class TripRestController {
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private CheckPointsRepository checkPointsRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private RoadRepository roadRepository;
	
	@Autowired
	private TruckRepository truckRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@RequestMapping(value = "/saveTrip/{truck_id}/{date}/{dest_id}/{driver_id}/{parent_id}/{source_id}/{road_id}", method = RequestMethod.GET)
	public boolean saveTrip(@PathVariable String truck_id,@PathVariable String date,@PathVariable long dest_id,@PathVariable long driver_id,@PathVariable long parent_id,@PathVariable long source_id,@PathVariable long road_id) 
	{
		Truck truck=truckRepository.findOne(truck_id);
		Road road=roadRepository.findOne(road_id);
//		
//		if(parent_id==0)
//		{
//			parent=null;
//		}
//		else
//		{
//			parent=tripRepository.findOne(parent_id);
//		}
		Driver driver=driverRepository.findOne(driver_id);
		Location dest=locationRepository.findOne(dest_id);
		Location source=locationRepository.findOne(source_id);
		Trip trip=new Trip();
		trip.setRate(5.0);
		trip.setDate(date);
		trip.setDriver(driver);
		trip.setDestination(dest);
		trip.setParent(parent_id);
		trip.setSource(source);
		trip.setTruck(truck);
		trip.setRoad(road);
		if(tripRepository.save(trip)!=null)
		{
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "/returnTrip/{driver_id}", method = RequestMethod.GET)
	public ArrayList<Location> saveTripRoad(@PathVariable long driver_id)
	{
		Driver driver=driverRepository.findOne(driver_id);
		ArrayList<Trip> allTrips=(ArrayList<Trip>)tripRepository.findAll();
		Trip trip=new Trip();
		for( int i=0;i<allTrips.size();i++)
		{
			if(allTrips.get(i).getDriver()==driver)
			{
				trip=allTrips.get(i);
			}
		}
		long road_id=trip.getRoad().getRoad_id();
		Road road =roadRepository.findOne(road_id);
		ArrayList<CheckPoints> locations=new ArrayList<CheckPoints>();
		ArrayList<CheckPoints> checkPoints=(ArrayList<CheckPoints>)checkPointsRepository.findAll();
		for(int i=0;i<checkPoints.size();i++)
		{
			if(checkPoints.get(i).getRoad()==road)
			{
				locations.add(checkPoints.get(i));
			}
		}
		
		ArrayList<Location> roadLocation=new ArrayList<Location>();
		for(int i=0;i<locations.size();i++)
		{
			if(locations.get(i).getTrip()==trip)
			{
				roadLocation.add(locations.get(i).getLocation());
			}
		}
	
		return roadLocation;
	}
	
	@RequestMapping(value="/getAllTrips",method=RequestMethod.GET)
	public ArrayList<Trip> getAllTrips()
	{
		return (ArrayList<Trip>)tripRepository.findAll();
	}
}
