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

import com.example.Repostitory.GoodRepository;
import com.example.Repostitory.TripGoodRepository;
import com.example.Repostitory.TripRepository;
import com.example.Repostitory.TruckRepository;
import com.example.models.Good;
import com.example.models.Trip;
import com.example.models.Truck;
import com.example.models.TripGood;

@RestController
@CrossOrigin(origins = "*")
public class GoodRestController {

	@Autowired
	private GoodRepository goodRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private TripGoodRepository tripGoodRepository;
	
	@Autowired
	private TripRepository tripRepository;



	
	@RequestMapping(value = "/saveGoods/{name}/{company}/{barcode}/{date}/{num_of_goods}", method = RequestMethod.GET)
	public Map<String ,Object> saveGoods(@PathVariable String name, @PathVariable String company, @PathVariable String barcode,
			@PathVariable String date, @PathVariable int num_of_goods) {
		Map<String, Object> res = new HashMap<>();
		if (isValidDate(date)) {
			DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			Date startDate = null;
			try {
				startDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Good good = new Good();
			good.setBarcode(barcode);
			good.setCompany(company);
			good.setDate(startDate);
			good.setName(name);
			good.setNum_of_goods(num_of_goods);
			good.setState(0);
			good.setDeleted(false);
			if (goodRepository.save(good) != null)
			{
				res.put("Success", "Saved!");
			}
			else
			{
				res.put("Error", "Connect Error!");
			}
				
		}
		else
		{
			res.put("Error", "Invalid Date!");
		}
		return res;

	}

	@RequestMapping(value = "/getAllGoods", method = RequestMethod.GET)
	public Map<String, Object> getAllGoods() {
		Map<String, Object> res = new HashMap<>();
		ArrayList<Good> goods = new ArrayList<Good>();
		if( goodRepository.findAll()!=null)
		{
			ArrayList<Good> Allgoods = (ArrayList<Good>) goodRepository.findAll();
			for (int i = 0; i < Allgoods.size(); i++) 
			{
				if (Allgoods.get(i).isDeleted() == false) 
				{
					goods.add(Allgoods.get(i));
				}
			}
			if(!goods.isEmpty())
			{
				res.put("Success",goods);
			}
			else
			{
				res.put("Error","All is deleted!");
			}
		}
		else
		{
			res.put("Error","There are no goods saved!");
		}
		
		return res;
	}

	@RequestMapping(value = "/getGood/{barcode}", method = RequestMethod.GET)
	public Map<String,Object> getGood(@PathVariable String barcode) {
		Map<String, Object> res = new HashMap<>();
		if (goodRepository.findOne(barcode) == null) {
			res.put("Error", "There is no good with this barcode");
		}
		else
		{
			Good good = goodRepository.findOne(barcode);
			if (good.isDeleted() == true) {
				res.put("Error","This good is deleted!");
			}
			else
			{
				res.put("Success",good);
			}
		}
		return res;
	}

	@RequestMapping(value = "/getGoodByTruckID/{truck_id}", method = RequestMethod.GET)
	public Map<String,Object> getGoodByTruckID(@PathVariable String truck_id) {
		Map<String, Object> res = new HashMap<>();
		if (truckRepository.findOne(truck_id) == null) {
			res.put("Error", "There is no Truck with this id");
		}
		else
		{
			Truck truck = truckRepository.findOne(truck_id);
			if (truck.getDeleted() == true) {
				res.put("Error", "This Truck is deleted!");
			}
			else
			{
				if(truck.getActive()==true)
				{
							
					if( tripRepository.findFirstByTruckOrderByIdDesc(truck)!=null)
					{
						Trip trip = tripRepository.findFirstByTruckOrderByIdDesc(truck);
						if(tripGoodRepository.findAllGoodsByTrip(trip)!=null)
						{
							ArrayList<Good> tripGoods=(ArrayList<Good>)tripGoodRepository.findAllGoodsByTrip(trip);
							res.put("Success",tripGoods );
						}
						else
						{
							res.put("Error","There are no goods for this trip");
						}	
					}
					else
					{
						res.put("Error","There is no trip for this truck");
					}
				}
				else
				{
					res.put("Error","This truck is not active!");
				}
				
			}
		}
		return res;

	}
	
	
	@RequestMapping(value = "/getGoodTrips/{barcode}", method = RequestMethod.GET)
	public Map<String,Object> getGoodTrips(@PathVariable String barcode) {
		Map<String, Object> res = new HashMap<>();
		if (goodRepository.findOne(barcode) == null) {
			res.put("Error","There is no good with this barcode!");
		}
		else
		{
			Good good = goodRepository.findOne(barcode);
			if(tripGoodRepository.findAllTripsByGood(good)!=null)
			{
				ArrayList <Trip> trips = tripGoodRepository.findAllTripsByGood(good);
				res.put("Success", trips);
			}
			else
			{
				res.put("Error","There are no trips for this good!");
			}
			
		}
		return res;
		
	}
	
	@RequestMapping(value = "/deleteAllGoods", method = RequestMethod.GET)
	public Map<String,Object> deleteAllGoods() {
		Map<String, Object> res = new HashMap<>();
		boolean flag=true;
		if(goodRepository.findAll()!=null)
		{
			ArrayList<Good> goods = (ArrayList<Good>) goodRepository.findAll();
			for (int i = 0; i < goods.size(); i++) 
			{
				goods.get(i).setDeleted(true);
				if (goodRepository.save(goods.get(i)) == null)
				{
					flag=false;
					break;
				}
			}
			if (flag==true)
			{
				res.put("Success", "All goods are deleted");
				
			}
			else
			{
				res.put("Error","Connection Error");
			}	
		}
		else
		{
			res.put("Error","There are no goods saved!");
		}
		
		return res;
	}

	@RequestMapping(value = "/deleteGood/{barcode}", method = RequestMethod.GET)
	public Map<String,Object> deleteGood(@PathVariable String barcode) {
		Map<String, Object> res = new HashMap<>();
		if (goodRepository.findOne(barcode) == null) {
			res.put("Error","There is no good with this barcode!");
		}
		else {
			Good good = goodRepository.findOne(barcode);
			good.setDeleted(true);
			if (goodRepository.save(good) != null)
			{
				res.put("Success", "Good id deleted!");
			}
			else
			{
				res.put("Error","Connection Error");
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
	
	
	@RequestMapping(value = "/updateGood/{barcode}/{new_barcode}", method = RequestMethod.GET)
	public Map<String, String> updateGood(@PathVariable String barcode,@PathVariable String new_barcode) {
		Map<String, String> res = new HashMap<>();
		Good good = goodRepository.findOne(barcode);
		if (good == null) {
			res.put("Error", "there's no good with That barcode");
		} else {
			Good good_ = goodRepository.findOne(new_barcode);
			if(good_==null)
			{
				if (good.isDeleted()) {
					res.put("Error", "there's no good with That barcode");
				} else {
					good.setId(new_barcode);
					if (goodRepository.save(good) != null)
						res.put("Success", "good is Updated");

				}
			}
			else
			{
				res.put("Error", "This good barcode in used");
			}
			
		}
		return res;
	}
	

}
