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
import com.example.models.Truck;

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
	
	@RequestMapping(value = "/{name}/{ssn}/{password}/saveDriver", method = RequestMethod.GET)
	public Map<String, String> saveDriver(@PathVariable String name, @PathVariable String ssn,
			@PathVariable String password) {
		Map<String, String> res = new HashMap<>();
		Driver driver_ = driverRepository.findBySsn(ssn);
		if (driver_ != null) {
			if (driver_.getDeleted()) {
				driver_.setName(name);
				driver_.setSsn(ssn);
				driver_.setPassword(password);
				driver_.setDeleted(false);
				driver_.setLogged(false);
				driver_.setRate(5.0);
				if (driverRepository.save(driver_) != null)
					res.put("Success", driver_.getDriver_id()+"");
				else
				res.put("Error", "error in connection to Server");
			} else
				// error that ssn in my system
				res.put("Error", "there is driver with that ssn");
		} else {
			Driver driver = new Driver();
			driver.setName(name);
			driver.setSsn(ssn);
			driver.setPassword(password);
			driver.setDeleted(false);
			driver.setLogged(false);
			driver.setRate(5.0);
			if (driverRepository.save(driver) != null)
				res.put("Success", driver.getDriver_id()+"");
			else
			res.put("Error", "error in connection to Server");
		}
		return res;
	}
	

	public double getDistanceBetweenTwoTruck(Driver first, Driver second) {
		Location truckOneLocation = locationRepository.findFirstByDriverOrderByIdDesc(first);
		Location truckTwoLocation = locationRepository.findFirstByDriverOrderByIdDesc(second);

		if (truckOneLocation != null && truckTwoLocation != null)
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

	@RequestMapping(value="/updateToken/{driverId}/{token}")
	public Map<String,Object> updateToken(@PathVariable long driverId,@PathVariable String token)
	{
		Map<String,Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driverId);
		if(driver!=null)
		{
			driver.setToken(token);
			driverRepository.save(driver);
			res.put("Success", "Done!!");
		}
		else
		{
			res.put("Error", "No Driver With that ID!!");
		}
		return res;
	}
	/* calculate penalty during trip */
	// Amina
	@RequestMapping(value = "/calculateSpeedPenalty/{tripId}", method = RequestMethod.GET)
	public Map<String,Double> calculateSpeedPenalty(@PathVariable long tripId) {
		double civilSpeed=90.0;
		Trip t = tripRepository.findOne(tripId);
		System.out.println("id="+t.getTruck().getId());
		Truck truck=t.getTruck();
		Location location = new Location();

		location=locationRepository.findFirstByTruckOrderByIdDesc(truck);//trc.getCurrentLocation(truck.getId());
		
		
	
		Penalties p = new Penalties();
		
		location.setSpeed(70.0);
		double diffrence = location.getSpeed() - civilSpeed;
		double penalty = 0.0;
		for (int i = 10; i <= diffrence; i += 10) {
			penalty += 0.1;
		}
		p.setLocation(location);
		p.setTrip(t);
		p.setType("speed");
		p.setValue(penalty);
		penaltiesRepostitory.save(p);
		Map<String,Double> res=new HashMap<>();
		if(penaltiesRepostitory.findByTrip(t)!=null)
			res.put("driver total rate is", tripRate(tripId)); 
		else
			res.put("Error",tripRate(tripId));
		return res;
	}

	// Amina
	@RequestMapping(value = "/calculateBrakePenalty/{previousSpeed}/{currentSpeed}/{tripId}", method = RequestMethod.GET)
	public Map<String,Double> calculateBrakePenalty(@PathVariable double previousSpeed, @PathVariable double currentSpeed, @PathVariable long tripId) {
		Trip trip = tripRepository.findOne(tripId);
		Truck truck=trip.getTruck();
		Location location = new Location();
		location=locationRepository.findFirstByTruckOrderByIdDesc(truck);
		double diffrence = Math.abs(previousSpeed - currentSpeed);

		if (diffrence >= 50) {
			Penalties p = new Penalties();
			p.setLocation(location);
			p.setTrip(trip);
			p.setType("brake");
			p.setValue(0.2);
			penaltiesRepostitory.save(p);
		
		}
		Map<String,Double> res=new HashMap<>();
		
		if(penaltiesRepostitory.findByTrip(trip)!=null)
			res.put("driver total rate is", tripRate(tripId)); 
		else
			res.put("Error",tripRate(tripId));
		return res;

	}

	// get penalty, update driver rate and set tripRate
	/* by amina */
	@RequestMapping(value = "/tripRate/{tripId}", method = RequestMethod.GET)
	public double tripRate(@PathVariable long tripId) {
		Trip trip = tripRepository.findOne(tripId);
		double tripRate = 5.0;
		ArrayList<Penalties> ps = penaltiesRepostitory.findByTrip(trip);
		for (int i = 0; i < ps.size(); i++) {
			
			
			tripRate -= ps.get(i).getValue();
			
		}

		trip.setRate(tripRate);
		tripRepository.save(trip);
		


		return tripRate;
	}
	//calculating driver total rate by Amina 
	@RequestMapping(value="/driverRate/{tripId}",method=RequestMethod.GET)
	public double driverRate (@PathVariable long tripId)
	{
		Trip trip = tripRepository.findOne(tripId);
		Driver driver = trip.getDriver();
		
		ArrayList<Trip> driverTrips = tripRepository.findByDriver(driver);
		double sum = 0.0;
		for (int i = 0; i < driverTrips.size(); i++) {
			sum += driverTrips.get(i).getRate();
		}

		double driverTotalRate = (double) sum / driverTrips.size();
		driver.setRate(driverTotalRate);
		driverRepository.save(driver);
		
		return driverTotalRate;
	}

	@RequestMapping(value = "/getAllDrivers", method = RequestMethod.GET)
	public ArrayList<Driver> getAllDrivers() {
		return driverRepository.findByDeleted(false);
	}
	// get trucks speed by their id only then calculate the accident probability

	@RequestMapping(value = "/getDriver/{driver_id}", method = RequestMethod.GET)
	public Map<String, Object> getDriver(@PathVariable long driver_id) {
		Map<String, Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driver_id);
		if (driver == null) {
			res.put("Error", "there's no dirver with that Id");
		} else {
			if (driver.getDeleted() == true) {
				res.put("Error", "this Driver are deleted");
			} else
				res.put("Success", driver);
		}
		return res;
	}

	@RequestMapping(value = "/deleteAllDrivers", method = RequestMethod.GET)
	public Map<String, String> deleteAllDrivers() {
		Map<String, String> res = new HashMap<>();
		ArrayList<Driver> drivers = (ArrayList<Driver>) driverRepository.findAll();
		for (int i = 0; i < drivers.size(); i++) {
			drivers.get(i).setDeleted(true);
			if (driverRepository.save(drivers.get(i)) == null)
				res.put("Error", "error in connection to Server");
		}
		if (res.isEmpty())
			res.put("Success", "Drivers Deleted!");
		return res;
	}

	@RequestMapping(value = "/deleteDriver/{driver_id}", method = RequestMethod.GET)
	public Map<String, String> deleteDriver(@PathVariable long driver_id) {
		Map<String, String> res = new HashMap<>();
		if (driverRepository.findOne(driver_id) == null) {
			res.put("Error", "Wrong Driver Id");
		} else {
			Driver driver = driverRepository.findOne(driver_id);
			if (driver.getDeleted()) {
				res.put("Error", "Wrong Driver Id");
			} else {
				// to keep history of driver
				driver.setDeleted(true);
				if (driverRepository.save(driver) != null)
					res.put("Success", "Driver Deleted!");
			}
		}
		return res;
	}

	/* if the manager wants to save a driver from the web site */

	
	@RequestMapping(value = "/updateDriver/{ssn}", method = RequestMethod.GET)
	public Map<String, String> updateDriver(@PathVariable long driver_id) {
		Map<String, String> res = new HashMap<>();
		if (driverRepository.findOne(driver_id) == null) {
			res.put("Error", "Wrong Driver Id");
		} else {
			Driver driver = driverRepository.findOne(driver_id);
			if (driver.getDeleted()) {
				res.put("Error", "Wrong Driver Id");
			} else {
				// to keep history of driver
				driver.setDeleted(true);
				if (driverRepository.save(driver) != null)
					res.put("Success", "Driver Deleted!");
			}
		}
		return res;
	}
	
	@RequestMapping(value = "/updateDriver/{driver_id}/{name}/{ssn}/{logged}/{token}/{password}/{deleted}", method = RequestMethod.GET)
	public boolean updateDriver(@PathVariable long driver_id,@PathVariable String name,@PathVariable String ssn
			,@PathVariable int logged,@PathVariable String token,@PathVariable String password
			,@PathVariable int deleted)
	{
		if(driverRepository.findOne(driver_id)==null)
		{
			return false;
		}
		Driver driver =driverRepository.findOne(driver_id);
		boolean flag;
		if(logged==1)
		{
			flag=true;
			driver.setLogged(flag);
		}
		else if(logged==0)
		{
			flag=false;
			driver.setLogged(flag);
		}
		if(deleted==1)
		{
			flag=true;
			driver.setDeleted(flag);
		}
		else if(deleted==0)
		{
			flag=false;
			driver.setDeleted(flag);
		}
		driver.setName(name);
		driver.setPassword(password);
		driver.setPassword(password);
		driver.setToken(token);
		driver.setSsn(ssn);
		if(driverRepository.save(driver)!=null)
		{
			return true;
		}
		return false;
	}

}
