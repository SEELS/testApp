package com.example.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Repostitory.DriverRepository;
import com.example.Repostitory.LocationRepository;
import com.example.models.Driver;
import com.example.models.Location;

@RestController
public class TruckController {

	@Autowired
	DriverRepository driverRepository;

	@Autowired
	LocationRepository locationRepository;



}
