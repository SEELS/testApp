
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

import com.example.Repostitory.CheckPointsRepository;
import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.Truck;
import com.example.models.CheckPoints;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Trip;

@RestController
@CrossOrigin(origins = "*")
public class TruckRestController {

	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private CheckPointsRepository checkPointsRepository;

	@Autowired
	private TripRepository tripRepository;

	/*
	 * it gets all the active trucks, helping to draw markers of all the active
	 * trucks
	 */
	@RequestMapping(value = "/getActiveTrucks", method = RequestMethod.GET)
	public ArrayList<Truck> getAllActiveTrucks() {
		ArrayList<Truck> trucks = (ArrayList<Truck>) truckRepository.findAll();
		ArrayList<Truck> activeTrucks = new ArrayList<Truck>();
		for (int i = 0; i < trucks.size(); i++) {
			if (trucks.get(i).getActive() == true) {
				activeTrucks.add(trucks.get(i));
			}
		}
		return activeTrucks;
	}

	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	/* it gets an object of a specific truck, helping to get truck data */
	@RequestMapping(value = "/SetDriverToTruck/{driver_id}/{Truck_id}", method = RequestMethod.GET)
	public Map<String, String> SetDriverToTruck(@PathVariable String Truck_id, @PathVariable long driver_id) {
		Map<String, String> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driver_id);
		if (driver == null) {
			res.put("Error", "Driver Not found");
		} else {
			Truck truck = truckRepository.findOne(Truck_id);
			if (truck == null) {
				res.put("Error", "truck Not found");
			} else {
				truck.setDriver(driver);
				if (truckRepository.save(truck) != null)
					res.put("Success", "Done !!");
				else
					res.put("Error", "Error Update in dataBase");
			}
		}
		return res;
	}

	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	/* it gets an object of a specific truck, helping to get truck data */
	@RequestMapping(value = "/changeTruckState/{state}/{Truck_id}", method = RequestMethod.GET)
	public Map<String, String> changeTruckstate(@PathVariable String Truck_id, @PathVariable boolean state) {
		Map<String, String> res = new HashMap<>();
		Truck truck = truckRepository.findOne(Truck_id);
		if (truck == null) {
			res.put("Error", "Truck Not found");
		} else {
			truck.setActive(state);
			if (truckRepository.save(truck) == null) {
				res.put("Error", "Error in update in database !!");
			} else
				res.put("Success", "Done !!");
		}
		return res;
	}

	/* it gets an object of a specific truck, helping to get truck data */
	@RequestMapping(value = "/viewTruck/{id}", method = RequestMethod.GET)
	public Map<String, Object> getSpecificTruck(@PathVariable String id) {
		Map<String, Object> res = new HashMap<>();
		Truck truck = truckRepository.findOne(id);
		if (truck == null)
			res.put("Error", "there's no truck with that Id");
		else
			res.put("Success", truck);
		return res;
	}

	/* it gets the current location of truck to draw a marker on the map */
	@RequestMapping(value = "/viewTruckLocation/{truck_id}", method = RequestMethod.GET)
	public Map<String, Object> getCurrentLocation(@PathVariable String truck_id) {
		Map<String, Object> res = new HashMap<>();
		System.out.println("in fun");
		System.err.println("tr"+truck_id);

		Truck truck = truckRepository.findById(truck_id);
		System.err.println("tr"+truck_id);
		if (truck == null)
			res.put("Error","there's no truck with that Id");
		else {
			res.put("Success", locationRepository.findFirstByTruckOrderByIdDesc(truck));
		}
		return res;
	}

	// /* it gets the current location of truck to draw a marker on the map */
	// @RequestMapping(value = "/viewTruckLocation/{truck_id}", method =
	// RequestMethod.GET)
	// public Location getCurrentLocation(@PathVariable String truck_id) {
	// ArrayList<Location> locations = (ArrayList<Location>)
	// locationRepository.findAll();
	// Truck truck = truckRepository.findOne(truck_id);
	// ArrayList<Location> truckLocations = new ArrayList<Location>();
	// for (int i = 0; i < locations.size(); i++) {
	// if (locations.get(i).getTruck() == truck) {
	// truckLocations.add(locations.get(i));
	// }
	// }
	// return truckLocations.get(truckLocations.size() - 1);
	// }

	/* it gets all locations of a truck, helping to get check points */
	@RequestMapping(value = "/viewTruckLocations/{id}/{trip_id}", method = RequestMethod.GET)
	public ArrayList<Location> getLocations(@PathVariable String id, @PathVariable long trip_id) {
		ArrayList<CheckPoints> checkPoint = (ArrayList<CheckPoints>) checkPointsRepository.findAll();
		ArrayList<CheckPoints> checkPoints = new ArrayList<CheckPoints>();
		Trip trip = tripRepository.findOne(trip_id);
		for (int i = 0; i < checkPoint.size(); i++) {
			if (checkPoint.get(i).getTrip() == trip) {
				checkPoints.add(checkPoint.get(i));
			}
		}
		ArrayList<Location> locations = new ArrayList<Location>();
		for (int i = 0; i < checkPoints.size(); i++) {
			locations.add(checkPoints.get(i).getLocation());
		}
		return locations;
	}

	@RequestMapping(value = "/viewDriverData/{truckId}", method = RequestMethod.GET)
	public Map<String, Object> getDriver(@PathVariable String truckId) {
		Map<String, Object> res = new HashMap<>();
		Truck truck = truckRepository.findOne(truckId);
		if (truck == null) {
			res.put("Error", "there's no truck with that Id");
		} else {
			Driver driver = truck.getDriver();
			if (driver == null)
				res.put("Error", "this truck not have Driver yet!!");
			else
				res.put("Success", driver);
		}
		return res;
	}

	/*
	 * Deleted Driver when save truck not need we can use assignDriverToTruck
	 * Service to assign that driver to truck Delete Active when save truck not
	 * need we can use changeTruckState To activate and deActivate
	 */

	@RequestMapping(value = "/{id}/saveTruck", method = RequestMethod.GET)
	public Map<String, String> saveTruck(@PathVariable String id) {

		Map<String, String> res = new HashMap<>();
		Truck truck_ = truckRepository.findOne(id);

		if (truck_ != null) {
			if (truck_.getDeleted()) {
				truck_.setDriver(null);
				truck_.setActive(false);
				truck_.setId(id);
				truck_.setDeleted(false);
				if (truckRepository.save(truck_) == null)
					res.put("Error", "error in connection to Server");
				else
					res.put("Success", "Done!");
			} else {
				res.put("Error", "there's Truck with that Id");
			}
		} else {
			Truck truck = new Truck();
			truck.setDriver(null);
			truck.setActive(false);
			truck.setId(id);
			truck.setDeleted(false);
			if (truckRepository.save(truck) == null)
				res.put("Error", "error in connection to Server");
			else
				res.put("Success", "Done!");
		}
		return res;
	}

	/* check if there will be a frontal crushing with cars */
	@RequestMapping(value = "/{truck1_id}/{truck2_id}/changeInSpeed", method = RequestMethod.GET)
	public boolean changeInSpeed(@PathVariable String truck1_id, @PathVariable String truck2_id) {
		ArrayList<Trip> trips = (ArrayList<Trip>) tripRepository.findAll();
		Truck truck1 = truckRepository.findOne(truck1_id);
		Driver driver1 = new Driver();
		for (int i = 0; i < trips.size(); i++) {
			if (trips.get(i).getTruck() == truck1) {
				driver1 = trips.get(i).getDriver();
			}
		}
		Truck truck2 = truckRepository.findOne(truck2_id);
		Driver driver2 = new Driver();
		for (int i = 0; i < trips.size(); i++) {
			if (trips.get(i).getTruck() == truck2) {
				driver2 = trips.get(i).getDriver();
			}
		}

		ArrayList<Location> locations = (ArrayList<Location>) locationRepository.findAll();
		ArrayList<Location> locaDriver1 = new ArrayList<Location>();
		ArrayList<Location> locaDriver2 = new ArrayList<Location>();
		for (int i = 0; i < locations.size(); i++) {
			if (locations.get(i).getDriver() == driver1) {
				locaDriver1.add(locations.get(i));
			}
			if (locations.get(i).getDriver() == driver2) {
				locaDriver2.add(locations.get(i));
			}
		}
		Location truckOneLocation = getCurrentLocation(driver1);
		Location truckTwoLocation = getCurrentLocation(driver2);
		// Location truckOneLocation = locaDriver1.get(locaDriver1.size()-1);
		// Location truckTwoLocation = locaDriver2.get(locaDriver2.size()-1);
		double truck1Speed = truckOneLocation.getSpeed();
		double truck2Speed = truckTwoLocation.getSpeed();
		double dist = getDistance(truckOneLocation, truckTwoLocation);

		if (dist <= 1000)
			return true; // Possible accident, distance <= 1km
		else if (truck2Speed <= truck1Speed)
			return false; // No Accident
		else {
			double newLat1 = truckOneLocation.getLat() + truck1Speed;
			double newLon1 = truckOneLocation.getLon() + truck1Speed;
			double newLat2 = truckTwoLocation.getLat() + truck1Speed;
			double newLon2 = truckTwoLocation.getLon() + truck1Speed;
			// System.out.println(newLat1 + "\t" + newLat2);
			// System.out.println(newLon1 + "\t" + newLon2);
			if (newLat2 >= newLat1 || newLon2 >= newLon1)
				return true; // Possible Accident
								// if truck2 speed: 50, location:60, truck1
								// speed:30, location:120
								// after 3 hrs the condition will be true that
								// there'll be a possible clash
		}
		return false;

	}

	private Location getCurrentLocation(Driver driver) {
		// TODO Auto-generated method stub
		return locationRepository.findFirstByDriverOrderByIdDesc(driver);
	}

	/* modified by amina */
	@RequestMapping(value = "/{truckId}/getCurrentTrip", method = RequestMethod.GET)
	public Map<String, Object> getCurrentTrip(@PathVariable String truckId) {
		Map<String, Object> res = new HashMap<>();

		Truck truck = truckRepository.findOne(truckId);
		if (truck == null) {
			res.put("Error","there's no truck with that Id");
		} else {

			ArrayList<Trip> trips = (ArrayList<Trip>) tripRepository.findAll();
			ArrayList<Trip> truckTrips = new ArrayList<Trip>();
			for (int i = 0; i < trips.size(); i++) {
				if (truck == trips.get(i).getTruck()) {
					truckTrips.add(trips.get(i));
				}
			}
			res.put("Success", truckTrips.get(truckTrips.size() - 1));
		}

		return res;
	}

	// Modified by Randa
	@RequestMapping(value = "/{truckId}/getTruckTrip", method = RequestMethod.GET)
	public Map<String, String> getTruckTrip(@PathVariable String truckId) {
		Map<String, String> res = new HashMap<>();
		Truck truck = truckRepository.findOne(truckId);
		if (truck == null) {
			res.put("Error", "There's no Truck with that Id");
		} else {
			ArrayList<Trip> trips = (ArrayList<Trip>) tripRepository.findAll();
			ArrayList<Trip> truckTrips = new ArrayList<Trip>();
			for (int i = 0; i < trips.size(); i++) {
				if (truck.equals(trips.get(i).getTruck())) {
					truckTrips.add(trips.get(i));
				}
			}
			res.put("Success", truckTrips.get(truckTrips.size() - 1).getTrip_id() + "");
		}

		return res;
	}

	@RequestMapping(value = "/getTruck/{truck_id}", method = RequestMethod.GET)
	public Map<String, Object> getTruck(@PathVariable String truck_id) {
		Map<String, Object> res = new HashMap<>();
		if (truckRepository.findOne(truck_id) == null) {
			res.put("Error", "there's no Truck with that Id");
		}
		Truck truck = truckRepository.findOne(truck_id);
		if (truck.getDeleted() == true) {
			res.put("Error", "this truck are deleted");
		} else
			res.put("Success", truck);
		return res;
	}

	@RequestMapping(value = "/getAllTrucks", method = RequestMethod.GET)
	public ArrayList<Truck> getAllTrucks() {
		return truckRepository.findByDeleted(false);
	}

	@RequestMapping(value = "/deleteAllTrucks", method = RequestMethod.GET)
	public Map<String, String> deleteAllTrucks() {
		Map<String, String> res = new HashMap<>();
		ArrayList<Truck> trucks = (ArrayList<Truck>) truckRepository.findAll();
		for (int i = 0; i < trucks.size(); i++) {
			trucks.get(i).setDeleted(true);
			if (truckRepository.save(trucks.get(i)) == null)
				res.put("Error", "error in connection to Server");
		}
		if (res.isEmpty())
			res.put("Success", "Trucks Deleted!");
		return res;
	}

	@RequestMapping(value = "/deleteTruck/{truck_id}", method = RequestMethod.GET)
	public Map<String, String> deleteTruck(@PathVariable String truck_id) {
		Map<String, String> res = new HashMap<>();
		if (truckRepository.findOne(truck_id) == null) {
			res.put("Error", "there's no Truck with That Id");
		} else {
			Truck truck = truckRepository.findOne(truck_id);
			if (truck.getDeleted()) {
				res.put("Error", "there's no Truck with That Id");
			} else {
				truck.setDeleted(true);
				if (truckRepository.save(truck) != null)
					res.put("Success", "Truck are Deleted");

			}
		}
		return res;
	}

	private double rad(double x) {
		return x * Math.PI / 180;
	}

	private double getDistance(Location p1, Location p2) {
		double R = 6378137; // Earthâ€™s mean radius in meter
		double dLat = rad(p2.getLat() - p1.getLat());
		double dLong = rad(p2.getLon() - p1.getLon());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(rad(p1.getLat())) * Math.cos(rad(p2.getLat())) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d; // return distance in meter
	}
	
	
	@RequestMapping(value = "/updateTruck/{truck_id}/{new_truck_id}", method = RequestMethod.GET)
	public Map<String, String> updateTruck(@PathVariable String truck_id,@PathVariable String new_truck_id) {
		Map<String, String> res = new HashMap<>();
		Truck truck = truckRepository.findOne(truck_id);
		if (truck == null) {
			res.put("Error", "there's no Truck with That Id");
		} else {
			Truck truck_ = truckRepository.findOne(new_truck_id);
			if(truck_==null)
			{
				if (truck.getDeleted()) {
					res.put("Error", "there's no Truck with That Id");
				} else {
					truck.setId(new_truck_id);
					if (truckRepository.save(truck) != null)
						res.put("Success", "Truck are Updated");

				}
			}
			else
			{
				res.put("Error", "This Truck Id in used");
			}
			
		}
		return res;
	}
	
	

}
