package com.example.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.CheckPointsRepository;
import com.example.Repostitory.RoadRepository;
import com.example.Repostitory.TripRepository;
import com.example.models.CheckPoints;
import com.example.models.Driver;
import com.example.models.Location;
import com.example.models.Road;
import com.example.models.Trip;


@RestController
@CrossOrigin(origins = "*")
public class RoadRestController {
	
	@Autowired
	private RoadRepository roadRepository;
	
	@Autowired
	private CheckPointsRepository checkPointsRepository;

	@Autowired
	private TripRepository tripRepository;
	
	/* saving road with specific trip. after the road's locations had been saved i have to assign a road to a trip */
	@RequestMapping(value = "/assignRoadForTrip/{trip_id}", method = RequestMethod.GET)
	public boolean saveRoad(@PathVariable long trip_id) 
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
		Road newRoad=roadRepository.findOne(saveRoadID());
		for(int i=0;i<checkPoints.size();i++)
		{
			checkPoints.get(i).setRoad(newRoad);
		}
		if (checkPointsRepository.save(checkPoints) != null)
			
			return true;
		return false;

	}
	public long saveRoadID()
	{
		Road newRoad=new Road();
		newRoad.setState(0);
		if(roadRepository.save(newRoad)!= null)
			return newRoad.getRoad_id();
		return 0;
	}
	
	@RequestMapping(value = "/getRoad/{trip_id}", method = RequestMethod.GET)
	public ArrayList<Location> getRoad(@PathVariable long trip_id) 
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
	
	@RequestMapping(value = "/getAllRoad", method = RequestMethod.GET)
	public ArrayList<Location> getAllRoad() 
	{
		ArrayList<CheckPoints> checkPoints=(ArrayList<CheckPoints>)checkPointsRepository.findAll();
		ArrayList<Location> locations=new ArrayList<Location>();
		for(int i=0;i<checkPoints.size();i++)
		{
			locations.add(checkPoints.get(i).getLocation());
		}
		return locations;
	}
	
	@RequestMapping(value = "/saveRoad/{state}", method = RequestMethod.GET)
	public boolean saveRoadObject(@PathVariable double state)
	{
		Road road=new Road();
		road.setState(state);
		if(roadRepository.save(road) != null)
		{
			return true;
		}
		return false;
	}
	
	@RequestMapping(value="/getAllRoads",method=RequestMethod.GET)
	public ArrayList<Road> getAllRoads()
	{
		return (ArrayList<Road>)roadRepository.findAll();
	}

	
	
}
