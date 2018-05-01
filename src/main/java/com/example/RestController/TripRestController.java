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

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.RoadRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;

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
	private RoadRepository roadRepository;

	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private LocationRepository locationRepository;

	//Modified by Mariam 
	//Modified By Sameh
	@RequestMapping(value = "/saveTrip/{truck_id}/{driver_id}/{parent_id}/{road_id}/{date}", method = RequestMethod.GET)
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
				ArrayList<Location> roadLocations=locationRepository.findByRoad(road);
				Driver driver = driverRepository.findOne(driver_id);
				if (driver != null) {
					Date date_ = getDate(date);
					Trip trip = new Trip();
					trip.setRate(5.0);
					trip.setDate(date_);
					trip.setDriver(driver);
					trip.setDestination(roadLocations.get(1));
					trip.setSource(roadLocations.get(0));
					trip.setParent(parent_id);
					trip.setTruck(truck);
					trip.setRoad(road);
					trip.setState(1);
					if (tripRepository.save(trip) != null) {
						res.put("Success", "Trip are added");
					} else
						res.put("Error", "Trip not save Database Error");
					
				} else {
					res.put("Error", "Driver Not found");
				}

			}
		}

		return res;
	}
	//Modified by Sameh
	@RequestMapping(value = "/returnTrip/{trip_id}", method = RequestMethod.GET)
	public ArrayList<Location> saveTripRoad(@PathVariable long trip_id) {
		Trip trip = tripRepository.findOne(trip_id);
		Road road=trip.getRoad();
		if(locationRepository.findByRoad(road).size()>2)
			return locationRepository.findByRoad(road);
		else
			return new ArrayList<>();
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
			String temp =dateFormat.format(date);
			Date mydate = getDate(temp);
			ArrayList<Trip> trips = tripRepository.findByDriverAndDeletedAndDateGreaterThanEqual(driver, false, mydate);
			if (trips == null) {
				res.put("Error", "There's no Trips to that Driver");
			} 
			else if(trips.isEmpty()) {
				res.put("Error", "There's no Trips to that Driver");
			}
			else {
				res.put("Success", trips.get(0).getTrip_id());
			}
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
						if(trip.getState()==1 )
						{
							trip.setState(2);
							if(tripRepository.save(trip)!=null)
								res.put("Success", "Your trip in process now");
							else
								res.put("Error", "Error Conection to Server");
						}
						else
							res.put("Error", "this trip are completed ");
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
						if(tripRepository.save(trip)!=null)
							res.put("Success", "Done!!");
						else
							res.put("Error", "Error Conection to Server");
					}
				}

			}
		}
		return res;
	}

	@RequestMapping(value = "/getAllTrips", method = RequestMethod.GET)
	public ArrayList<Trip> getAllTrips() {
		return (ArrayList<Trip>) tripRepository.findByDeleted(false);
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
	
	@RequestMapping(value = "/tripLocations/{trip_id}", method = RequestMethod.GET)
	public Map<String,Object> TripLocations(@PathVariable long trip_id) {
		Map<String,Object> res = new HashMap<>();
		Trip trip = tripRepository.findOne(trip_id);
		if (trip == null) {
			res.put("Error", "Error Conection to Server");
		} else {
			ArrayList<Location> locations = locationRepository.findByTrip(trip);
			if(locations==null)
			{
				res.put("Error", "There's No Locations to this Trip");
			}
			else if(locations.isEmpty())
			{
				res.put("Error", "There's No Locations to this Trip");
			}
			else
			{
				res.put("Success", locations);
			}
		}
		return res;
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
	
	public static Date getDate(String date)
	{
		if(isValidDate(date))
		{
			DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			Date Date = null;
			try {
				Date = df.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Date;
		}
		return null;
	}

	public Map<String, String> SetDriverToTruck(String Truck_id,long driver_id) {
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

	public Map<String, String> changeTruckstate(String Truck_id,boolean state) {
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
	
	
}
