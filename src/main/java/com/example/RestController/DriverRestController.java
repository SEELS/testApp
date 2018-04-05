package com.example.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.PenaltiesRepostitory;
import com.example.Repostitory.TripRepository;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Penalties;
import com.example.models.Trip;

@RestController
@CrossOrigin(origins = "*")
public class DriverRestController {
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private PenaltiesRepostitory penaltiesRepostitory;
	
	@Autowired
	private TripRepository tripRepository;

	
	public double getDistanceBetweenTwoTruck(Driver first, Driver second) {
		Location truckOneLocation = locationRepository.findFirstByDriverOrderByIdDesc(first);
		Location truckTwoLocation = locationRepository.findFirstByDriverOrderByIdDesc(second);
		
		if (truckOneLocation!=null &&truckTwoLocation!=null)
			return getDistance(truckOneLocation, truckTwoLocation);
		else
			return Double.MAX_VALUE;
	}

	private double rad(double x) {
		return x * Math.PI / 180;
	}

	private double getDistance(Location p1, Location p2) {
		System.err.println(p1.toString() + p2.toString());
		double R = 6378137; // Earth’s mean radius in meter
		double dLat = rad(p2.getLat() - p1.getLat());
		double dLong = rad(p2.getLon() - p1.getLon());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(rad(p1.getLat())) * Math.cos(rad(p2.getLat())) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d; // return distance in meterd
	}

	/* get nearest Trucks to my truck in specific range */
	@RequestMapping(value = "/getNearLocation/{driverId}/{range}", method = RequestMethod.GET)
	public ArrayList<Location> getNearTrucksToTruck(@PathVariable long driverId, @PathVariable double range) {
		
		ArrayList<Driver> nearestDriver = new ArrayList<>();
		ArrayList<Location> nearestDriverLocation = new ArrayList<>();
		ArrayList<Driver> allDriver = getAllDrivers();
		Driver myDriver = driverRepository.findOne(driverId);
		if (myDriver != null) {
			allDriver.remove(myDriver);
			for (Driver driver : allDriver) {
				if (getDistanceBetweenTwoTruck(myDriver, driver) <= range) {
					nearestDriver.add(driver);
				}
			}
			
			for (Driver driver : nearestDriver) {
				/* Query are more effective in time this service call more than 4 time in 1 minute    
				
				ArrayList<Location> allLocations=(ArrayList<Location>)locationRepository.findAll();
				ArrayList<Location> firstDriverLocations=new ArrayList<Location>();
				for(int i=0;i<allLocations.size();i++)
				{
					if(allLocations.get(i).getDriver()==driver)
					{
						firstDriverLocations.add(allLocations.get(i));
					}
				}
				Location driverLocation=firstDriverLocations.get(firstDriverLocations.size()-1);
				*/
				Location driverLocation = locationRepository.findFirstByDriverOrderByIdDesc(driver);
				nearestDriverLocation.add(driverLocation);
			}
		}
		return nearestDriverLocation;
	}

	/* login for driver of the truck */
	@RequestMapping(value = "/login/{id}/{password}", method = RequestMethod.GET)
	public Map<String, Integer> login(@PathVariable long id, @PathVariable String password) {
		Map<String, Integer> temp = new HashMap<>();
		boolean logged = false;
		Driver driver = driverRepository.findOne(id);
		if (driver == null) {
			logged = false;
			temp.put("logged", 0);
			temp.put("error", 0);
			return temp;
		} else {
			if (driver.getPassword().equals(password)) {
				if (driver.getLogged())
					logged = true;
				else {
					driver.setLogged(true);
					logged = true;
					driverRepository.save(driver);
				}
			} else {
				// driver.setLogged(false);
				logged = false;
			}
			if (logged)
				temp.put("logged", 1);
			else {
				temp.put("logged", 0);
				temp.put("error", 1);
			}
			return temp;
		}
	}	
	
	/*calculate penalty during trip*/
	//Amina
	@RequestMapping(value="/calculateSpeedPenalty/{locationId}/{civilSpeed}/{tripId}",method=RequestMethod.GET)
	private void calculateSpeedPenalty (@PathVariable long locationId,@PathVariable double civilSpeed,@PathVariable long tripId)
	{
		Trip t=tripRepository.findOne(tripId);
		Location location = locationRepository.findOne(locationId);
		Penalties p=new Penalties();
		
			p=new Penalties();
			double diffrence=Math.abs(location.getSpeed()-civilSpeed);
			double penalty=0.0;
			for (int i=10;i<=diffrence;i+=10)
			{
				penalty+=0.1;
			}
			p.setLocation(location); p.setTrip(t); p.setType("speed"); p.setValue(penalty);
			penaltiesRepostitory.save(p);
	}
	//Amina
	@RequestMapping(value="/calculateBrakePenalty/{locationId}/{previousSpeed}/{currentSpeed}/{tripId}",method=RequestMethod.GET)
	public void calculateBrakePenalty(@PathVariable long locationId,@PathVariable double previousSpeed,@PathVariable double currentSpeed,@PathVariable long tripId)
	{
		Trip trip = tripRepository.findOne(tripId);
		Location location = locationRepository.findOne(locationId);
		double diffrence = Math.abs(previousSpeed-currentSpeed);
		
		if(diffrence>=50)
		{
			Penalties p= new Penalties();
			p.setLocation(location); p.setTrip(trip); p.setType("brake"); p.setValue(0.2);
			penaltiesRepostitory.save(p);
		}
		
	}
	
	
	
	
	//get penalty, update driver rate and set tripRate 
	/*by amina*/
	@RequestMapping(value="/rate/{tripId}",method=RequestMethod.GET)
	public double rate(@PathVariable long tripId)
	{	
		Trip trip=tripRepository.findOne(tripId);
		Driver driver=trip.getDriver();
		double tripRate=5.0;
		ArrayList<Penalties> ps = penaltiesRepostitory.findByTrip(trip);
		for (int i=0;i<ps.size();i++)
		{
			tripRate-=ps.get(i).getValue();
		}
		trip.setRate(tripRate);
		tripRepository.save(trip);
		ArrayList<Trip> driverTrips = tripRepository.findByDriver(driver);
		double sum=0.0;
		for (int i=0;i<driverTrips.size();i++)
		{
			sum+=driverTrips.get(i).getRate();
		}
		
		double driverTotalRate=(double)sum/driverTrips.size();
		driver.setRate(driverTotalRate);
		driverRepository.save(driver);
		
		return driverTotalRate;
	}
	
	
	@RequestMapping(value="/getAllDrivers",method=RequestMethod.GET)
	public ArrayList<Driver> getAllDrivers()
	{
		return (ArrayList<Driver>)driverRepository.findAll();
	}
//get trucks speed by their id only then calculate the accident probability 
	
	@RequestMapping(value="/getDriver/{}",method=RequestMethod.GET)
	public Driver getDriver(@PathVariable long driver_id)
	{
		if(driverRepository.findOne(driver_id)==null)
		{
			return null;
		}
		Driver driver =driverRepository.findOne(driver_id);
		if(driver.getDeleted()==true)
		{
			return null;
		}
		return driver;
	}
	
	@RequestMapping(value="/deleteAllDrivers",method=RequestMethod.GET)
	public boolean deleteAllDrivers()
	{
		ArrayList<Driver> drivers= (ArrayList<Driver>)driverRepository.findAll();
		for(int i=0;i<drivers.size();i++)
		{
			drivers.get(i).setDeleted(true);
			if(driverRepository.save(drivers.get(i))==null)
				return false;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteDriver/{driver_id}",method=RequestMethod.GET)
	public boolean deleteTruck(@PathVariable long driver_id)
	{
		if(driverRepository.findOne(driver_id)==null)
		{
			return false;
		}
		else
		{
			Driver driver=driverRepository.findOne(driver_id);
			driver.setDeleted(true);
			if(driverRepository.save(driver)!=null)
				return true;
		}
		return false;
	}

}
