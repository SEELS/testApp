package com.example.Repostitory;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.models.Good;

public interface GoodRepository extends CrudRepository<Good, String>{

}
