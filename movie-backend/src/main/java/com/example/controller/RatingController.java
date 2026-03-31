package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Rating;
import com.example.repository.RatingRepository;

@RestController
@RequestMapping("/ratings")
public class RatingController {

	@Autowired
	private RatingRepository ratingRepository;
	
	@PostMapping
    public Rating addRating(@RequestBody Rating rating){
        return ratingRepository.save(rating);
    }

    @GetMapping("/movie/{movieId}")
    public List<Rating> getRatingsByMovie(@PathVariable Integer movieId){
        return ratingRepository.findByMovieId(movieId);
    }

    @GetMapping("/user/{userId}")
    public List<Rating> getRatingsByUser(@PathVariable Integer userId){
        return ratingRepository.findByUserId(userId);
    }
}
