package com.example.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URL;

import org.json.JSONException;

import org.json.JSONObject;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.TripLocationRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.Truck;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Trip;
import com.example.models.TripLocation;


@RestController
@CrossOrigin(origins = "*")
public class TruckRestController {
	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private TripLocationRepository tripLocationRepository;
	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private TripRepository tripRepository;

	/*
	 * it gets all the active trucks, helping to draw markers of all the active
	 * trucks
	 */
	@RequestMapping(value = "/getActiveTrucks", method = RequestMethod.GET)
	public Map<String,Object> getAllActiveTrucks() {
		Map<String, Object> res = new HashMap<>();
		if( truckRepository.findAllByActive(true)!=null)
		{
			res.put("Success",truckRepository.findAllByActive(true));
		}
		else
		{
			res.put("Error","There are no active trucks");
		}
		
		return res;
	}
	
	//Amina 
	
	@RequestMapping(value="/getTruckTrip/{trip_id}",method=RequestMethod.GET)
	public Map<String,Object> getTruckTrip(@PathVariable long trip_id)
	{
		Map<String,Object> res = new HashMap<>();
		Trip trip= new Trip();
		if (tripRepository.findOne(trip_id)==null)
		{
			res.put("Error", "No trip with this id");
		}
		else 
		{
			trip=tripRepository.findOne(trip_id);
			Truck truck= trip.getTruck();
			if (truck==null)
			{
				res.put("Error","No truck for this trip ! ");
			}
			else
				
			{
				res.put("Success", truck.getId());
			}
			
		}
		return res;
		
		
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
	
	//modified by Mariam
	/* it gets the actual current location of truck to draw a marker on the map */
	@RequestMapping(value = "/viewTruckLocation/{driver_id}", method = RequestMethod.GET)
	public Map<String, Object> getCurrentLocation(@PathVariable long driver_id) {
		Map<String, Object> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driver_id);
		if (driver != null)
		{
			//res.put("Error","there's no truck with that Id");
			if(locationRepository.findFirstByDriverOrderByIdDesc(driver)!=null)
			{
				res.put("Success", locationRepository.findFirstByDriverOrderByIdDesc(driver));
			}
			else
			{
				res.put("Error","there's no location for this driver");
			}
		}   
		else {
			res.put("Error","there's no driver with that Id");
		}
		return res;
	}

	/* it gets all actual locations of a trip*/
	@RequestMapping(value = "/viewTripLocations/{trip_id}", method = RequestMethod.GET)
	public Map<String,Object> viewTripLocations(@PathVariable long trip_id) {
		Map<String, Object> res = new HashMap<>();
		if(tripRepository.findOne(trip_id)!=null)
		{
			Trip trip = tripRepository.findOne(trip_id);
			if(tripLocationRepository.findAllLocationsByTrip(trip)!=null)
			{
				ArrayList<Location> truckLocations = tripLocationRepository.findAllLocationsByTrip(trip);
				res.put("Success", truckLocations);
			}
			else
			{
				res.put("Error", "There is no locations for this trip");
			}
		}
		else
		{
			res.put("Error", "There is no trip with this id");
		}
		return res;
	}
	
	/* it gets all actual locations of a truck*/
	//Amina modified
	@RequestMapping(value = "/viewTruckLocations/{truck_id}", method = RequestMethod.GET)
	public Map<String,Object> viewTrukLocations(@PathVariable String truck_id) {
		Map<String, Object> res = new HashMap<>();
		if(truckRepository.findOne(truck_id)!=null)
		{
			Truck truck = truckRepository.findOne(truck_id);
			Trip trip=new Trip();
			if(tripRepository.findByTruckAndState(truck,2)!=null)
			{
				trip = tripRepository.findByTruckAndState(truck,2);
				if(tripLocationRepository.findAllLocationsByTrip(trip)!=null)
				{
					ArrayList<Location> truckLocations = tripLocationRepository.findAllLocationsByTrip(trip);
					res.put("Success", truckLocations);
				}
				else
					res.put("Error", "There are no locations for this trip");
			}
			else
				res.put("Error", "There is no trip with this truck and state");

		}else
			res.put("Error", "There is no truck with this id");
		
		return res;
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
	/* modified by mariam */
	//modified by Amina
	@RequestMapping(value = "/{truck1_id}/{truck2_id}/changeInSpeed", method = RequestMethod.GET)
	public Map<String,Object> changeInSpeed(@PathVariable String truck1_id, @PathVariable String truck2_id) {
		
		Map<String, Object> res = new HashMap<>();
		Truck truck1 = truckRepository.findOne(truck1_id);
		Truck truck2 = truckRepository.findOne(truck2_id);
		//error 
		Driver driver1 = tripRepository.findLastByTruckAndState(truck1,2).getDriver();
		Driver driver2 = tripRepository.findLastByTruckAndState(truck2,2).getDriver();
//		ArrayList<Location> locaDriver1 = locationRepository.findAllByDriver(driver1);
//		ArrayList<Location> locaDriver2 =  locationRepository.findAllByDriver(driver2);
		Location truckOneLocation = getCurrentLocation(driver1);
		Location truckTwoLocation = getCurrentLocation(driver2);
		double truck1Speed = truckOneLocation.getSpeed();
		double truck2Speed = truckTwoLocation.getSpeed();
		double dist = getDistance(truckOneLocation, truckTwoLocation);
		//get drivers Tokens
		String tokenDriver1=driver1.getToken();
		String tokenDriver2=driver2.getToken();
		String Key="AIzaSyBrcdEhjh8S2NbfjCKzvUnxpK6PmiCYTfw";
		String Message="be carefull ya ramaaaaa, you will have an accident soon isA 🙂";
		//check if they are on the same direction
		if (dist <= 1000)
		{
			send_FCM_Notification(tokenDriver1,Key,Message);
			send_FCM_Notification(tokenDriver2,Key,Message);
			res.put("Possible accident", "There are will have an accident soon"); // Possible accident, distance <= 1km
		}
		else {		
			//by assuming they are in the same direction
			double Time=dist/(truck1Speed+truck2Speed);
			if(Time>1)
			{
				Message="there is a driver named "+driver1.getName() +" who you will meet after: "+Time +" hr";
				send_FCM_Notification(tokenDriver1,Key,Message);
				Message="there is a driver named "+driver2.getName() +" who you will meet after: "+Time +" hr";
				send_FCM_Notification(tokenDriver2,Key,Message);
				res.put("No possible accident", "There will meet after: "+Time+" hr" );
			}
			else
			{
				Time=Time*60;
				Message="there is a driver named "+driver1.getName() +" who you will meet after: "+Time +" Min";
				send_FCM_Notification(tokenDriver1,Key,Message);
				Message="there is a driver named "+driver2.getName() +" who you will meet after: "+Time+ " Min";
				send_FCM_Notification(tokenDriver2,Key,Message);
				res.put("No possible accident", "There will meet after: "+Time+" Min" );
			}
		}
		
		
//		else {
//			double newLat1 = truckOneLocation.getLat() + truck1Speed;
//			double newLon1 = truckOneLocation.getLon() + truck1Speed;
//			double newLat2 = truckTwoLocation.getLat() + truck1Speed;
//			double newLon2 = truckTwoLocation.getLon() + truck1Speed;
//			// System.out.println(newLat1 + "\t" + newLat2);
//			// System.out.println(newLon1 + "\t" + newLon2);
//			if (newLat2 >= newLat1 || newLon2 >= newLon1)
//			{
//				send_FCM_Notification(tokenDriver1,Key,Message);
//				send_FCM_Notification(tokenDriver2,Key,Message);
//				return true; // Possible Accident
//								// if truck2 speed: 50, location:60, truck1
//								// speed:30, location:120
//								// after 3 hrs the condition will be true that
//			}			// there'll be a possible clash
//		}
		return res;

	}

	private Location getCurrentLocation(Driver driver) {
		return locationRepository.findFirstByDriverOrderByIdDesc(driver);
	}

//	/* modified by amina */
//	@RequestMapping(value = "/{truckId}/getCurrentTrip", method = RequestMethod.GET)
//	public Map<String, Object> getCurrentTrip(@PathVariable String truckId) {
//		Map<String, Object> res = new HashMap<>();
//
//		Truck truck = truckRepository.findOne(truckId);
//		if (truck == null) {
//			res.put("Error","there's no truck with that Id");
//		} else {
//
//			ArrayList<Trip> trips = (ArrayList<Trip>) tripRepository.findAll();
//			ArrayList<Trip> truckTrips = new ArrayList<Trip>();
//			for (int i = 0; i < trips.size(); i++) {
//				if (truck == trips.get(i).getTruck()) {
//					truckTrips.add(trips.get(i));
//				}
//			}
//			res.put("Success", truckTrips.get(truckTrips.size() - 1));
//		}
//
//		return res;
//	}


	// Modified by Randa
	@RequestMapping(value = "/{truckId}/getTruckTrip", method = RequestMethod.GET)
	public Map<String, String> getTruckTrip(@PathVariable String truckId) {
		Map<String, String> res = new HashMap<>();
		Truck truck = truckRepository.findOne(truckId);
		if (truck == null) {
			res.put("Error", "There's no Truck with that Id");
		} else {
			if(tripRepository.findAllByTruck(truck)!=null)
			{
				ArrayList<Trip> truckTrips = tripRepository.findAllByTruck(truck);
				res.put("Success", truckTrips.get(truckTrips.size() - 1).getTrip_id() + "");
			}
			else
				res.put("Error", "There is no trip for this truck");
			
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
	public Map<String,Object> getAllTrucks() {
		Map<String, Object> res = new HashMap<>();
		if(truckRepository.findByDeleted(false)!=null)
		{
			res.put("Success", truckRepository.findByDeleted(false));
		}
		else
			res.put("Error", "There is no trucks saved ");
		return res;
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
		double R = 6378137; // Earthأ¢â‚¬â„¢s mean radius in meter
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
	
	
	public void send_FCM_Notification(String tokenId, String server_key, String message){
		try{
		URL url = new URL("https://fcm.googleapis.com/fcm/send");
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key="+server_key);
		conn.setRequestProperty("Content-Type","application/json");
		JSONObject infoJson = new JSONObject();
		infoJson.put("title","Here is your notification.");
		infoJson.put("body", message);
		JSONObject json = new JSONObject();
		json.put("to",tokenId.trim());
		json.put("notification", infoJson);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		int status = 0;
		if( null != conn ){
		status = conn.getResponseCode();
		}
		if( status != 0){
		if( status == 200 ){
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + reader.readLine());
		}else if(status == 401){
		System.out.println("Notification Response : TokenId : " + tokenId + " Error occurred :");
		}else if(status == 501){
		System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + tokenId);
		}else if( status == 503){
		System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + tokenId);

		}
		}

		}catch(MalformedURLException mlfexception){
		System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(IOException mlfexception){
		System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(JSONException jsonexception){
		System.out.println("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());
		}catch (Exception exception) {
		System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
		}
		}

}