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

import com.example.Repostitory.LocationRepository;
import com.example.Repostitory.RoadRepository;
import com.example.models.Location;
import com.example.models.Road;


@RestController
@CrossOrigin(origins = "*")
public class RoadRestController {
	
	@Autowired
	private RoadRepository roadRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	
	@RequestMapping(value = "/saveRoad/{name}/{dlat}/{dlon}/{slat}/{slon}/{state}", method = RequestMethod.GET)
	public Map<String, Object> saveRoadObject(@PathVariable double state,@PathVariable String name,@PathVariable double dlat,@PathVariable double dlon,@PathVariable double slat,@PathVariable double slon)
	{
		Map<String,Object> res = new HashMap<>();
		Road road=new Road();
		road.setState(1);
		road.setName(name);
		if(roadRepository.save(road) == null)
		{
			res.put("Error", "error in connection to Server");
		}
		else
		{
			Location source = new Location();
			source.setLat(slat);
			source.setLon(slon);
			source.setSpeed(0.0);
			source.setRoad(road);
			Location destination = new Location();
			destination.setLat(dlat);
			destination.setLon(dlon);
			destination.setSpeed(0.0);
			destination.setRoad(road);
			if(locationRepository.save(source)==null || locationRepository.save(destination)==null)
			{
				res.put("Error", "error in connection to Server");
			}
			else
			{
				res.put("Success",road.getId());
			}
			
		}
		return res;
	}
	
	@RequestMapping(value = "/getRoad/{road_id}", method = RequestMethod.GET)
	public Map<String, Object> getRoad(@PathVariable long road_id) 
	{
		Map<String,Object> res = new HashMap<>();
		Road road = roadRepository.findOne(road_id);
		if(road==null)
		{
			res.put("Error", "there's no Road with that Id");
		}
		else
		{
			ArrayList<Location> roadLocations = (ArrayList<Location>)locationRepository.findAll();
			if(roadLocations ==null)
			{
				res.put("Error", "there's no Location in this road");
			}
			else if(roadLocations.isEmpty() || roadLocations.size()==2)
			{
				res.put("Error", "this A new Road his trip not started yet");
			}
			else
			{
				ArrayList<Location> locations = new ArrayList<>();
				for (int i = 0; i < roadLocations.size(); i++) {
					if(roadLocations.get(i).getRoad()==road)
					locations.add(roadLocations.get(i));
				}
				res.put("Success",locations);
			}
		}
		return res;
	}
	
	
	@RequestMapping(value="/getAllRoads",method=RequestMethod.GET)
	public ArrayList<Location> getAllRoads()
	{
		ArrayList<Location> allRoadLocations=(ArrayList<Location>)locationRepository.findAll();
		ArrayList<Location> roadsLocarions=new ArrayList<Location>();
		for(int i=0;i<allRoadLocations.size();i++)
		{
			if(allRoadLocations.get(i).getRoad()!=null)
			{
				roadsLocarions.add(allRoadLocations.get(i));
			}
		}
		return roadsLocarions;
	}


	@RequestMapping(value="/deleteAllRoads",method=RequestMethod.GET)
	public boolean deleteAllRoads()
	{
		ArrayList<Road> roads= (ArrayList<Road>)roadRepository.findAll();
		for(int i=0;i<roads.size();i++)
		{
			roads.get(i).setDeleted(true);
			if(roadRepository.save(roads.get(i))==null)
				return false;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteRoad/{road_id}",method=RequestMethod.GET)
	public boolean deleteRoad(@PathVariable long road_id)
	{
		if(roadRepository.findOne(road_id)==null)
		{
			return false;
		}
		else
		{
			Road road=roadRepository.findOne(road_id);
			road.setDeleted(true);
			if(roadRepository.save(road)!=null)
				return true;
		}
		return false;
	}
	
	
}
