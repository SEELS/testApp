package com.example.RestController;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.Driver;
import com.example.models.Location;
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

	/* saving location with a specific driver and speed */
	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	@RequestMapping(value = "/{lat}/{lon}/{speed}/{driver_id}/saveLocation", method = RequestMethod.GET)
	public Map<String, String> getLocation(@PathVariable Double lat, @PathVariable Double lon,
			@PathVariable Double speed, @PathVariable long driver_id) {
		Map<String, String> res = new HashMap<>();

		Location l = new Location();
		l.setLat(lat);
		l.setLon(lon);
		l.setSpeed(speed);
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
				else
					res.put("Success", "location are added");
			}
		}
		return res;
	}

	/*
	 * get current location of the recent trips. it will get the active location of
	 * a trips
	 */
	@RequestMapping(value = "/getCurrentLocation", method = RequestMethod.GET)
	public Location getCurrentLocation() {
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
			return temp;
		}
		return null;

	}

	@RequestMapping(value = "/getAllLocations", method = RequestMethod.GET)
	public ArrayList<Location> getAllLocations() {
		return (ArrayList<Location>) locationRepository.findAll();

	}

}
