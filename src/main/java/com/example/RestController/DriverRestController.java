package com.example.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.controller.TruckController;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Truck;

@RestController
@CrossOrigin(origins = "*")
public class DriverRestController {
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	
	
	public double getDistanceBetweenTwoTruck(Driver first,Driver second) {
		Location truckOneLocation = locationRepository.findFirstByDriverOrderByIdDesc(first);
		Location truckTwoLocation = locationRepository.findFirstByDriverOrderByIdDesc(second);
		return getDistance(truckOneLocation, truckTwoLocation);
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
		return d; // return distance in meter
	}
	
	/* get nearest Trucks to my truck in specific range*/
	@RequestMapping(value = "/getNearLocation/{driverId}/{range}", method = RequestMethod.GET)
	public ArrayList<Location> getNearTrucksToTruck(@PathVariable long driverId,@PathVariable double range)
	{
		ArrayList<Driver> nearestDriver = new ArrayList<>();
		ArrayList<Location> nearestDriverLocation = new ArrayList<>();
		TruckController truckController = new TruckController();
		ArrayList<Driver> allDriver = getAllDrivers();
		Driver myDriver =driverRepository.findOne(driverId); 
		allDriver.remove(myDriver);
		for (Driver driver : allDriver) {
			if (getDistanceBetweenTwoTruck(myDriver, driver)<=range) {
				nearestDriver.add(driver);
			}
		}
		
		for (Driver driver : nearestDriver) {
			Location driverLocation = locationRepository.findFirstByDriverOrderByIdDesc(driver);
			nearestDriverLocation.add(driverLocation);
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
					logged = false;
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
	@RequestMapping(value="/getAllDrivers",method=RequestMethod.GET)
	public ArrayList<Driver> getAllDrivers()
	{
		return (ArrayList<Driver>)driverRepository.findAll();
	}
//get trucks speed by their id only then calculate the accident probability 
	
	

}
