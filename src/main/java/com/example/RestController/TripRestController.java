package com.example.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private RoadRepository roadRepository;

	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private DriverRepository driverRepository;

	@RequestMapping(value = "/saveTrip/{truck_id}/{driver_id}/{parent_id}/{road_id}", method = RequestMethod.GET)
	public Map<String, String> saveTrip(@PathVariable String truck_id, @PathVariable String date,
			@PathVariable long driver_id, @PathVariable long parent_id, @PathVariable long road_id) {
		Map<String, String> res = new HashMap<>();

		Truck truck = truckRepository.findOne(truck_id);
		Road road = roadRepository.findOne(road_id);
		if (truck == null) {
			res.put("Error", "there's No Truck with that Id");
		} else {
			if (road == null) {
				res.put("Error", "there's No Road with that Id");
			} else {
				ArrayList<CheckPoints> checkPoints = checkPointsRepository.findByRoad(road);
				Driver driver = driverRepository.findOne(driver_id);
				if (driver != null) {
					Trip trip = new Trip();
					trip.setRate(5.0);
					if (isValidDate(date)) {
						DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
						Date startDate = null;
						try {
							startDate = df.parse(date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						trip.setDate(startDate);
						trip.setDriver(driver);
						trip.setDestination(checkPoints.get(1).getLocation());
						trip.setSource(checkPoints.get(0).getLocation());
						trip.setParent(parent_id);
						trip.setTruck(truck);
						trip.setRoad(road);
						if (tripRepository.save(trip) != null) {
							res.put("Success", "Trip are added");
						} else
							res.put("Error", "Trip not save Database Error");
					}
				} else {
					res.put("Error", "Driver Not found");
				}

			}
		}

		return res;
	}

	@RequestMapping(value = "/returnTrip/{driver_id}", method = RequestMethod.GET)
	public ArrayList<Location> saveTripRoad(@PathVariable long driver_id) {
		Driver driver = driverRepository.findOne(driver_id);
		ArrayList<Trip> allTrips = (ArrayList<Trip>) tripRepository.findAll();
		Trip trip = new Trip();
		for (int i = 0; i < allTrips.size(); i++) {
			if (allTrips.get(i).getDriver() == driver) {
				trip = allTrips.get(i);
			}
		}
		long road_id = trip.getRoad().getRoad_id();
		Road road = roadRepository.findOne(road_id);
		ArrayList<CheckPoints> locations = new ArrayList<CheckPoints>();
		ArrayList<CheckPoints> checkPoints = (ArrayList<CheckPoints>) checkPointsRepository.findAll();
		for (int i = 0; i < checkPoints.size(); i++) {
			if (checkPoints.get(i).getRoad() == road) {
				locations.add(checkPoints.get(i));
			}
		}

		ArrayList<Location> roadLocation = new ArrayList<Location>();
		for (int i = 0; i < locations.size(); i++) {
			if (locations.get(i).getTrip() == trip) {
				roadLocation.add(locations.get(i).getLocation());
			}
		}

		return roadLocation;
	}

	@RequestMapping(value = "/driverTrip/{driverId}", method = RequestMethod.GET)
	public Map<String, Object> getDriverTrip(@PathVariable long driverId) {
		Map<String, Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driverId);
		if (driver == null) {
			res.put("Error", "There's no driver with that Id");
		} else {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			ArrayList<Trip> trips = tripRepository.findByDriverAndDateGreaterThanEqual(driver, date);
			if (trips == null) {
				res.put("Error", "There's no Trips to that Driver");
			} else {
				res.put("Success", trips.get(0).getTrip_id());
			}
		}
		return res;
	}

	/* it gets an object of a specific truck, helping to get truck data */
	@RequestMapping(value = "/SetDriverToTruck/{driver_id}/{Truck_id}", method = RequestMethod.GET)
	public Map<String, String> SetDriverToTruck(@PathVariable String Truck_id, @PathVariable long driver_id) {
		Map<String, String> res = new HashMap<>();
		if (driver_id == 0) {
			Truck truck = truckRepository.findOne(Truck_id);
			if (truck == null) {
				res.put("Error", "truck Not found");
			} else {
				truck.setDriver(null);
				if (truckRepository.save(truck) != null)
					res.put("Success", "Done !!");
				else
					res.put("Error", "Error Update in dataBase");
			}
		} else {
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
		}
		return res;
	}

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

	@RequestMapping(value = "/startTrip/{driverId}/{tripId}", method = RequestMethod.GET)
	public Map<String, Object> startTrip(@PathVariable long driverId, @PathVariable long tripId) {
		Map<String, Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driverId);
		if (driver == null) {
			res.put("Error", "There's no driver with that Id");
		} else {
			Trip trip = tripRepository.findOne(tripId);
			if (trip == null) {
				res.put("Error", "There's no Trip to that Driver");
			} else {
				Truck truck = trip.getTruck();
				Map<String, String> temp = SetDriverToTruck(truck.getId(), driverId);
				if (temp.containsKey("Error"))
					res.put("Error", temp.get("Error"));
				else {
					temp = changeTruckstate(truck.getId(), true);
					if (temp.containsKey("Error"))
						res.put("Error", temp.get("Error"));
					else {
						trip.setState(2);
					}
				}

			}
		}
		return res;
	}

	@RequestMapping(value = "/endTrip/{driverId}/{tripId}", method = RequestMethod.GET)
	public Map<String, Object> endTrip(@PathVariable long driverId, @PathVariable long tripId) {
		Map<String, Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driverId);
		if (driver == null) {
			res.put("Error", "There's no driver with that Id");
		} else {
			Trip trip = tripRepository.findOne(tripId);
			if (trip == null) {
				res.put("Error", "There's no Trip to that Driver");
			} else {
				Truck truck = trip.getTruck();
				Map<String, String> temp = SetDriverToTruck(truck.getId(), 0);
				if (temp.containsKey("Error"))
					res.put("Error", temp.get("Error"));
				else {
					temp = changeTruckstate(truck.getId(), false);
					if (temp.containsKey("Error"))
						res.put("Error", temp.get("Error"));
					else {
						trip.setState(0);
					}
				}

			}
		}
		return res;
	}

	@RequestMapping(value = "/getAllTrips", method = RequestMethod.GET)
	public ArrayList<Trip> getAllTrips() {
		return (ArrayList<Trip>) tripRepository.findAll();
	}

	@RequestMapping(value = "/getTrip/{trip_id}", method = RequestMethod.GET)
	public Trip getTrip(@PathVariable long trip_id) {
		if (tripRepository.findOne(trip_id) == null) {
			return null;
		}
		Trip trip = tripRepository.findOne(trip_id);
		if (trip.isDeleted() == true) {
			return null;
		}
		return trip;
	}

	@RequestMapping(value = "/deleteAllTrips", method = RequestMethod.GET)
	public boolean deleteAllTrips() {
		ArrayList<Trip> trips = (ArrayList<Trip>) tripRepository.findAll();
		for (int i = 0; i < trips.size(); i++) {
			trips.get(i).setDeleted(true);
			if (tripRepository.save(trips.get(i)) == null)
				return false;
		}
		return true;
	}

	@RequestMapping(value = "/deleteTrip/{trip_id}", method = RequestMethod.GET)
	public boolean deleteTrip(@PathVariable long trip_id) {
		if (tripRepository.findOne(trip_id) == null) {
			return false;
		} else {
			Trip trip = tripRepository.findOne(trip_id);
			trip.setDeleted(true);
			if (tripRepository.save(trip) != null)
				return true;
		}
		return false;
	}

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
}
