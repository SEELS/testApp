package com.example.RestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.GoodRepository;
import com.example.Repostitory.TripRepository;
import com.example.models.Good;
import com.example.models.Truck;

@RestController
@CrossOrigin(origins = "*")
public class GoodRestRepository {
	
	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private TripRepository tripRepository;

	@RequestMapping(value="/saveGoods",method=RequestMethod.GET)
	public boolean saveGoods(@PathVariable String name, @PathVariable String company, @PathVariable String barcode, @PathVariable String date, @PathVariable int num_of_goods)
	{
		Good good =new Good();
		good.setBarcode(barcode);
		good.setCompany(company);
		good.setDate(date);
		good.setName(name);
		good.setNum_of_goods(num_of_goods);
		good.setState(0);
		good.setDeleted(false);
		if (goodRepository.save(good) != null)
			return true;
		return false;
		
	}
	@RequestMapping(value="/getAllGoods",method=RequestMethod.GET)
	public ArrayList<Good> getAllGoods()
	{
		
		ArrayList<Good> goods=new ArrayList<Good>();
		ArrayList<Good> Allgoods=(ArrayList<Good>)goodRepository.findAll();;
		for(int i=0;i<Allgoods.size();i++)
		{
			if(Allgoods.get(i).getDeleted()==false)
			{
				goods.add(Allgoods.get(i));
			}
		}
		return goods;
	}
	
	@RequestMapping(value="/getGood/{barcode}",method=RequestMethod.GET)
	public Good getGood(@PathVariable String barcode)
	{
		if(goodRepository.findOne(barcode)==null)
		{
			return null;
		}
		Good good =goodRepository.findOne(barcode);
		if(good.getDeleted()==true)
		{
			return null;
		}
		return good;
	}
	
	@RequestMapping(value="/deleteAllGoods",method=RequestMethod.GET)
	public boolean deleteAllGoods()
	{
		ArrayList<Good> goods= (ArrayList<Good>)goodRepository.findAll();
		for(int i=0;i<goods.size();i++)
		{
			goods.get(i).setDeleted(true);
			if(goodRepository.save(goods.get(i))==null)
				return false;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteGood/{barcode}",method=RequestMethod.GET)
	public boolean deleteGood(@PathVariable String barcode)
	{
		if(goodRepository.findOne(barcode)==null)
		{
			return false;
		}
		else
		{
			Good good=goodRepository.findOne(barcode);
			good.setDeleted(true);
			if(goodRepository.save(good)!=null)
				return true;
		}
		return false;
	}

	
	


}
