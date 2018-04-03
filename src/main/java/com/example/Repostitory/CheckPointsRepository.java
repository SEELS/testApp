package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.CheckPoints;

public interface CheckPointsRepository extends CrudRepository<CheckPoints, Long>  {
}
