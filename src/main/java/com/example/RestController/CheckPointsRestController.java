package com.example.RestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.CheckPointsRepository;
import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.GoodRepository;
import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.RoadRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.CheckPoints;
import com.example.models.Driver;
import com.example.models.Good;
import com.example.models.Location;
import com.example.models.Road;
import com.example.models.Trip;
import com.example.models.Truck;

@RestController
@CrossOrigin(origins = "*")
public class CheckPointsRestController {

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private CheckPointsRepository checkPointsRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private GoodRepository goodRepository;
	@Autowired
	private RoadRepository roadRepository;
	
	
	/* if the manager wants to save the check points of the certain road with locations */
	@RequestMapping(value = "/saveCheckPoints/{trip_id}/{location_id}/{barcode}/{road_id}", method = RequestMethod.GET)
	public boolean saveCheckPoints(@PathVariable long trip_id,@PathVariable long road_id,@PathVariable long location_id,@PathVariable String barcode) 
	{
		Trip trip=tripRepository.findOne(trip_id);
		Location location=locationRepository.findOne(location_id);
		
		Road road;
		if(road_id==0)
		{
			road=null;
		}
		else
		{
			road=roadRepository.findOne(road_id);

		}
		
		Good good;
		if(barcode.isEmpty())
		{
			good=null;
		}
		else
		{
			 good=goodRepository.findOne(barcode);
		}
		CheckPoints checkPoints=new CheckPoints();
		checkPoints.setGood(good);
		checkPoints.setLocation(location);
		checkPoints.setTrip(trip);
		checkPoints.setRoad(road);
		checkPoints.setDeleted(false);
		if (checkPointsRepository.save(checkPoints) != null)
			return true;
		return false;
	}
	
	@RequestMapping(value="/getAllCheckPoints",method=RequestMethod.GET)
	public ArrayList<CheckPoints> getAllCheckPoints()
	{
		ArrayList<CheckPoints> checkPoints=new ArrayList<CheckPoints>();
		ArrayList<CheckPoints> AllCheckPoints=(ArrayList<CheckPoints>)checkPointsRepository.findAll();
		for(int i=0;i<AllCheckPoints.size();i++)
		{
			if(AllCheckPoints.get(i).getDeleted()==false)
			{
				checkPoints.add(AllCheckPoints.get(i));
			}
		}
		return checkPoints;
	}
	
	@RequestMapping(value="/getCheckPoints/{checkPoint_id}",method=RequestMethod.GET)
	public CheckPoints getCheckPoints(@PathVariable long checkPoint_id)
	{
		if(checkPointsRepository.findOne(checkPoint_id)==null)
		{
			return null;
		}
		CheckPoints checkPoints =checkPointsRepository.findOne(checkPoint_id);
		if(checkPoints.getDeleted()==true)
		{
			return null;
		}
		return checkPoints;
	}
	
	@RequestMapping(value="/deleteAllCheckPoints",method=RequestMethod.GET)
	public boolean deleteAllCheckPoints()
	{
		ArrayList<CheckPoints> checkPoints= (ArrayList<CheckPoints>)checkPointsRepository.findAll();
		for(int i=0;i<checkPoints.size();i++)
		{
			checkPoints.get(i).setDeleted(true);
			if(checkPointsRepository.save(checkPoints.get(i))==null)
				return false;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteCheckPoints/{checkPoints_id}",method=RequestMethod.GET)
	public boolean deleteCheckPoints(@PathVariable long checkPoint_id)
	{
		if(checkPointsRepository.findOne(checkPoint_id)==null)
		{
			return false;
		}
		else
		{
			CheckPoints checkPoints=checkPointsRepository.findOne(checkPoint_id);
			checkPoints.setDeleted(true);
			if(checkPointsRepository.save(checkPoints)!=null)
				return true;
		}
		return false;
	}
}
