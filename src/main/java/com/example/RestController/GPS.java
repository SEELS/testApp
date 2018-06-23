package com.example.RestController;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.PenaltiesRepostitory;
import com.example.Repostitory.TripLocationRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Penalties;
import com.example.models.Trip;
import com.example.models.TripLocation;
import com.example.models.Truck;

@RestController
@CrossOrigin(origins = "*")

public class GPS {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private DriverRepository driverRepository;
	@Autowired
	private TripLocationRepository tripLocationRepository;
	
	
	@Autowired
	private TripRepository tripRepository;

	/* saving location with a specific driver and speed */
	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	//modified by Mariam
	//modified by sameh

	@RequestMapping(value = "/{lat}/{lon}/{speed}/{driver_id}/{tripId}/{road_id}/saveLocation", method = RequestMethod.GET)
	public Map<String, Object> saveLocation(@PathVariable Double lat, @PathVariable Double lon,
			@PathVariable Double speed, @PathVariable long driver_id,@PathVariable long tripId,@PathVariable long road_id) {
		Map<String, Object> res = new HashMap<>();
		Location l = new Location();
		l.setLat(lat);
		l.setLon(lon);
		l.setSpeed(speed);
		Date date = new Date();
		l.setTime(date);
		Trip trip = new Trip();
		if(tripRepository.findOne(tripId)!=null)
			 
			trip=tripRepository.findOne(tripId);
		else
			res.put("Error", "trip not found");
		
		if(road_id!=0)
		{
			l.setRoad(trip.getRoad());
		}
		else
		{
			l.setRoad(null);
		}
		Driver driver = driverRepository.findOne(driver_id);
		if (driver == null) {
			res.put("Error", "Driver Not found");
		} else {
			l.setDriver(driver);
			Truck truck = truckRepository.findByDriver(driver);
			if (truck == null) {
				res.put("Error", "Driver Truck Not  found !!");
			} else {
				l.setTruck(truck);
				if(locationRepository.save(l)==null)
				{
					res.put("Error","Location not save Database Error");
				}
				else {
					truck.setPreviousSpeed(truck.getCurrentSpeed());
					truck.setCurrentSpeed(speed);
					if(truckRepository.save(truck) == null)
					{
						res.put("Error","Location not save Database Error");
					}
					else
						{
						
						TripLocation tripLocation = new TripLocation(l,trip);
						
						if(tripLocationRepository.save(tripLocation)!=null)
							res.put("Success", "location are added");
						else
							res.put("Error", "not saved");

						}
						
				}
			}
			PenaltiesRestController prs= new PenaltiesRestController();
			Map<String,Object> p=prs.calculateBrakePenalty(truck.getPreviousSpeed(), truck.getCurrentSpeed(), tripId);
			p=prs.calculateSpeedPenalty(tripId);
			if (p.containsKey("driver total rate is"))
			{
				res.put("rate: ", p.get("driver total rate is"));
			}
			else {
				res.put("Error in rate", p.get("Error"));
			}

		}
		return res;
	}

	/*
	 * get current location of the recent trips. it will get the active location of
	 * a trips
	 */
	@RequestMapping(value = "/getCurrentLocation", method = RequestMethod.GET)
	public Map<String ,Object> getCurrentLocation() {
		Map<String, Object> res = new HashMap<>();
		if(locationRepository.findAll()!=null)
		{
			ArrayList<Location> l = (ArrayList<Location>) locationRepository.findAll();
			Comparator<Location> locationComparator = new Comparator<Location>() {
				@Override
				public int compare(Location l1, Location l2) {
					return (int) (l1.getId() - l2.getId());
				}

			};
			Collections.sort(l, locationComparator);
			if (l.size() != 0) {
				Location temp = l.get(l.size() - 1);
				if (temp.getId() == 1) {
					temp = l.get(0);
				}
				res.put("Success", temp);
			}
			else
			{
				res.put("Error", "There are no locations saved!");
			}
		}
		else
		{
			res.put("Error", "There are no locations saved!");
		}
		return res;

	}

	@RequestMapping(value="/getAllLocations",method=RequestMethod.GET)
	public Map<String,Object> getAllLocations()
	{
		Map<String, Object> res = new HashMap<>();
		if(locationRepository.findAllByDeleted(false)!=null)
		{
			ArrayList<Location> locations=(ArrayList<Location>)locationRepository.findAllByDeleted(false);
			res.put("Success", locations);
		}
		else
		{
			res.put("Error", "There are no locations saved!");
		}
		return res;
	}
	
	@RequestMapping(value="/getLocation/{location_id}",method=RequestMethod.GET)
	public Map<String ,Object> getLocation(@PathVariable long location_id)
	{
		Map<String, Object> res = new HashMap<>();
		if(locationRepository.findOne(location_id)==null)
		{
			res.put("Error", "There is no location with id!");
			
		}
		else
		{
			Location location =locationRepository.findOne(location_id);
			if(location.getDeleted()==true)
			{
				res.put("Error","There location is deleted!" );
			}
			else
			{
				res.put("Success", location);
			}
		}
		
		return res;
	}
	
	@RequestMapping(value="/deleteAllLocations",method=RequestMethod.GET)
	public Map<String,Object> deleteAllLocations()
	{
		boolean flag=true;
		Map<String, Object> res = new HashMap<>();
		if(locationRepository.findAll()!=null)
		{
			res.put("Error","There are no locations saved!");
		}
		else
		{
			ArrayList<Location> locations= (ArrayList<Location>)locationRepository.findAll();
			for(int i=0;i<locations.size();i++)
			{
				locations.get(i).setDeleted(true);
				if(locationRepository.save(locations.get(i))==null)
					flag=false;
			}
			if(flag==false)
			{
				res.put("Error", "Connection Error!");
			}
			else
			{
				res.put("Success","All goods are deleted!");
			}
		}
		return res;
	}
	
	@RequestMapping(value="/deleteLocation/{Location_id}",method=RequestMethod.GET)
	public Map<String,Object> deleteLocation(@PathVariable long Location_id)
	{
		Map<String, Object> res = new HashMap<>();
		if(locationRepository.findOne(Location_id)==null)
		{
			res.put("Error","There is no Location With this id!");
		}
		else
		{
			Location location=locationRepository.findOne(Location_id);
			location.setDeleted(true);
			if(locationRepository.save(location)!=null)
				res.put("Success", "Location is deleted");
			else
				res.put("Error", "Connection Error");
		}
		return res;
	}
	
}