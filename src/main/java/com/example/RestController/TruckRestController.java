
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
	
	/* it gets all the active trucks, helping to draw markers of all the active trucks*/
	@RequestMapping(value = "/getActiveTrucks", method = RequestMethod.GET)
	public ArrayList<Truck> getAllActiveTrucks() {
		ArrayList<Truck> trucks = (ArrayList<Truck>) truckRepository.findAll();
		ArrayList<Truck> activeTrucks=new ArrayList<Truck>();
		for(int i=0;i<trucks.size();i++)
		{
			if(trucks.get(i).getActive()==true)
			{
				activeTrucks.add(trucks.get(i));
			}
		}
		return activeTrucks;
	}
	
	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	/* it gets an object of a specific truck, helping to get truck data*/
	@RequestMapping(value = "/SetDriverToTruck/{driver_id}/{Truck_id}", method = RequestMethod.GET)
	public Map<String, String> SetDriverToTruck(@PathVariable String Truck_id,@PathVariable long driver_id) {
		Map<String,String> res = new HashMap<>();
		Driver driver = driverRepository.findOne(driver_id);
		if(driver==null)
		{
			res.put("Error", "Driver Not found");
		}
		else
		{
			Truck truck = truckRepository.findOne(Truck_id);
			if(truck==null)
			{
				res.put("Error", "truck Not found");
			}
			else
			{
				truck.setDriver(driver);
				if(truckRepository.save(truck)!=null)
					res.put("Success", "Done !!");
				else
					res.put("Error", "Error Update in dataBase");
			}
		}
		return res;
	}
	
	
	// sara & sameh Edit 3/4/2018 1:20 Dr :Shawky
	/* it gets an object of a specific truck, helping to get truck data*/
	@RequestMapping(value = "/changeTruckState/{state}/{Truck_id}", method = RequestMethod.GET)
	public Map<String, String> changeTruckstate(@PathVariable String Truck_id,@PathVariable boolean state) {
		Map<String,String> res = new HashMap<>();
		Truck truck = truckRepository.findOne(Truck_id);
		if(truck==null)
		{
			res.put("Error", "Truck Not found");
		}
		else
		{
			truck.setActive(state);
			if(truckRepository.save(truck)==null)
			{
				res.put("Error", "Error in update in database !!");
			}
			else
				res.put("Success", "Done !!");
		}
		return res;
	}
	
	/* it gets an object of a specific truck, helping to get truck data*/
	@RequestMapping(value = "/viewTruck/{id}", method = RequestMethod.GET)
	public Truck getSpecificTruck(@PathVariable String id) {
		Truck truck = truckRepository.findOne(id);
		return truck;
	}
	
	/* it gets the current location of truck to draw a  marker on the map */
	@RequestMapping(value = "/viewTruckLocation/{truck_id}", method = RequestMethod.GET)
	public Location getCurrentLocation(@PathVariable String truck_id) {
		ArrayList<Location> locations=(ArrayList<Location>) locationRepository.findAll();
		Truck truck = truckRepository.findOne(truck_id);
		ArrayList<Location> truckLocations=new ArrayList<Location>();
		for(int i=0;i<locations.size();i++)
		{
			if(locations.get(i).getTruck()==truck)
			{
				truckLocations.add(locations.get(i));
			}
		}
		return truckLocations.get(truckLocations.size()-1);
	}
	
	
	/* it gets the current location of truck to draw a  marker on the map */
	@RequestMapping(value = "/viewCurrentLocation/{driver_id}", method = RequestMethod.GET)
	public Location getCurrentLocation(@PathVariable long driver_id) {
		ArrayList<Location> locations=(ArrayList<Location>) locationRepository.findAll();
		Driver driver = driverRepository.findOne(driver_id);
		ArrayList<Location> truckLocations=new ArrayList<Location>();
		for(int i=0;i<locations.size();i++)		
		{
			if(locations.get(i).getDriver()==driver)
			{
				truckLocations.add(locations.get(i));
			}
		}
		return truckLocations.get(truckLocations.size()-1);
	}
	/* it gets all locations of a truck, helping to get check points  */
	@RequestMapping(value = "/viewTruckLocations/{id}/{trip_id}", method = RequestMethod.GET)
	public ArrayList<Location> getLocations(@PathVariable String id,@PathVariable long trip_id)
	{
		ArrayList<CheckPoints> checkPoint=(ArrayList<CheckPoints>)checkPointsRepository.findAll();
		ArrayList<CheckPoints> checkPoints=new ArrayList<CheckPoints>();
		Trip trip=tripRepository.findOne(trip_id);
		for(int i=0;i<checkPoint.size();i++)
		{
			if(checkPoint.get(i).getTrip()==trip)
			{
				checkPoints.add(checkPoint.get(i));
			}
		}
		ArrayList<Location> locations=new ArrayList<Location>();
		for(int i=0;i<checkPoints.size();i++)
		{
			locations.add(checkPoints.get(i).getLocation());
		}
		return locations;
	}

	
	@RequestMapping(value = "/viewDriverData/{id}", method = RequestMethod.GET)
	public Driver getDriver(@PathVariable String id) 
	{
		Truck truck=truckRepository.findOne(id);
		return truck.getDriver();
	}
	
	@RequestMapping(value = "/{id}/{active}/{driver_id}/saveTruck", method = RequestMethod.GET)
	public boolean saveTruck(@PathVariable String id, @PathVariable boolean active , @PathVariable long driver_id) {
		Truck truck =new Truck();
		Driver driver=driverRepository.findOne(driver_id);
		truck.setDriver(driver);
		truck.setActive(active);
		truck.setId(id);
		truck.setDeleted(false);
		if (truckRepository.save(truck) != null)
			return true;
		return false;
	}
	

	/* if the manager wants to save a driver from the web site */
	@RequestMapping(value = "/{name}/{ssn}/{password}/saveDriver", method = RequestMethod.GET)
	public boolean saveDriver(@PathVariable String name , @PathVariable String ssn, @PathVariable String password) {
		Driver driver =new Driver();
		driver.setName(name);
		driver.setSsn(ssn);
		driver.setPassword(password);
		driver.setDeleted(false);
		driver.setLogged(false);
		if (driverRepository.save(driver) != null)
			return true;
		return false;
	}
	

	
	/*check if there will be a frontal crushing with cars  */
	@RequestMapping(value = "/{truck1_id}/{truck2_id}/changeInSpeed", method = RequestMethod.GET)
	public boolean changeInSpeed(@PathVariable String truck1_id,@PathVariable String truck2_id)
	{
		ArrayList<Trip> trips=(ArrayList<Trip>) tripRepository.findAll();
		Truck truck1=truckRepository.findOne(truck1_id);
		Driver driver1 =new Driver();
		for(int i=0;i<trips.size();i++)
		{
			if(trips.get(i).getTruck()==truck1)
			{
				driver1=trips.get(i).getDriver();
			}
		}
		Truck truck2=truckRepository.findOne(truck2_id);
		Driver driver2 =new Driver();
		for(int i=0;i<trips.size();i++)
		{
			if(trips.get(i).getTruck()==truck2)
			{
				driver2=trips.get(i).getDriver();
			}
		}

		ArrayList<Location> locations=(ArrayList<Location>)locationRepository.findAll();
		ArrayList<Location> locaDriver1=new ArrayList<Location>();
		ArrayList<Location> locaDriver2=new ArrayList<Location>();
		for( int i=0;i<locations.size();i++)
		{
			if(locations.get(i).getDriver()==driver1)
			{
				locaDriver1.add(locations.get(i));
			}
			if(locations.get(i).getDriver()==driver2)
			{
				locaDriver2.add(locations.get(i));
			}
		}
		Location truckOneLocation = getCurrentLocation(driver1.getDriver_id());
		Location truckTwoLocation = getCurrentLocation(driver2.getDriver_id());
		//Location truckOneLocation = locaDriver1.get(locaDriver1.size()-1);
		//Location truckTwoLocation = locaDriver2.get(locaDriver2.size()-1);
		double truck1Speed=truckOneLocation.getSpeed();
		double truck2Speed=truckTwoLocation.getSpeed();
		double dist=getDistance(truckOneLocation, truckTwoLocation);

		if (dist <=1000)
			return true;	// Possible accident, distance <= 1km
		else if (truck2Speed <= truck1Speed)
			return false; 		// No Accident
		else 
		{	
			double newLat1 = truckOneLocation.getLat()+truck1Speed ;
			double newLon1 = truckOneLocation.getLon()+truck1Speed ;
			double newLat2 = truckTwoLocation.getLat()+truck1Speed ;
			double newLon2 = truckTwoLocation.getLon()+truck1Speed ;
			//System.out.println(newLat1 + "\t" + newLat2);
			//System.out.println(newLon1 + "\t" + newLon2);
			if (newLat2>= newLat1 || newLon2 >= newLon1)
				return true;	// Possible Accident
								// if truck2 speed: 50, location:60, truck1 speed:30, location:120
								// after 3 hrs the condition will be true that there'll be a possible clash
		}
		return false;

	}

	/* modified by amina*/
	@RequestMapping(value="/{truckId}/getCurrentTrip",method=RequestMethod.GET)
	public Trip getCurrentTrip(@PathVariable String truckId)
	{

		Truck truck= truckRepository.findOne(truckId);
		ArrayList<Trip> trips=(ArrayList<Trip>)tripRepository.findAll();
		 ArrayList<Trip> truckTrips=new ArrayList<Trip>();
		 for (int i=0;i<trips.size();i++)
		 {
			 if(truck==trips.get(i).getTruck())
			 {
				 truckTrips.add(trips.get(i));
			 }
		 }
		 
		 return truckTrips.get(truckTrips.size()-1);
	}
	
	// Modified by Randa
	@RequestMapping(value="/{truckId}/getTruckTrip",method=RequestMethod.GET)
	public long getTruckTrip(@PathVariable String truckId)
	{

		Truck truck= truckRepository.findOne(truckId);
		ArrayList<Trip> trips=(ArrayList<Trip>)tripRepository.findAll();
		 ArrayList<Trip> truckTrips=new ArrayList<Trip>();
		 for (int i=0;i<trips.size();i++)
		 {
			 if(truck.equals(trips.get(i).getTruck()))
			 {
				 truckTrips.add(trips.get(i));
			 }
		 }
		 
		 return truckTrips.get(truckTrips.size()-1).getTrip_id();
	}

	@RequestMapping(value="/getTruck/{truck_id}",method=RequestMethod.GET)
	public Truck getTruck(@PathVariable String truck_id)
	{
		if(truckRepository.findOne(truck_id)==null)
		{
			return null;
		}
		Truck truck =truckRepository.findOne(truck_id);
		if(truck.getDeleted()==true)
		{
			return null;
		}
		return truck;
	}
	
	
	@RequestMapping(value="/getAllTrucks",method=RequestMethod.GET)
	public ArrayList<Truck> getAllTrucks()
	{
		ArrayList<Truck> trucks=new ArrayList<Truck>();
		ArrayList<Truck> AllTrucks=(ArrayList<Truck>)truckRepository.findAll();
		for(int i=0;i<AllTrucks.size();i++)
		{
			if(AllTrucks.get(i).getDeleted()==false)
			{
				trucks.add(AllTrucks.get(i));
			}
		}
		return trucks;
	}
	
	@RequestMapping(value="/deleteAllTrucks",method=RequestMethod.GET)
	public boolean deleteAllTrucks()
	{
		ArrayList<Truck> trucks= (ArrayList<Truck>)truckRepository.findAll();
		for(int i=0;i<trucks.size();i++)
		{
			trucks.get(i).setDeleted(true);
			if(truckRepository.save(trucks.get(i))==null)
				return false;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteTruck/{truck_id}",method=RequestMethod.GET)
	public boolean deleteTruck(@PathVariable String truck_id)
	{
		if(truckRepository.findOne(truck_id)==null)
		{
			return false;
		}
		else
		{
			Truck truck=truckRepository.findOne(truck_id);
			truck.setDeleted(true);
			if(truckRepository.save(truck)!=null)
				return true;
		}
		return false;
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
	

}
