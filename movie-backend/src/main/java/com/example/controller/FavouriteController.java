package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Favourite;
import com.example.entity.Movie;
import com.example.repository.FavoriteRepository;
import com.example.repository.MovieRepository;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins="http://localhost:4200")
public class FavouriteController {
	
	 @Autowired
	    private FavoriteRepository favoriteRepository;

	    @Autowired
	    private MovieRepository movieRepository;

	    @PostMapping
	    public Favourite addFavorite(@RequestBody Favourite fav){
	        return favoriteRepository.save(fav);
	    }

	    @GetMapping("/{userId}")
	    public List<Movie> getFavorites(@PathVariable Integer userId){

	        List<Favourite> favs = favoriteRepository.findByUserId(userId);

	        List<Integer> movieIds =
	                favs.stream()
	                    .map(Favourite::getMovieId)
	                    .toList();

	        return movieRepository.findAllById(movieIds);
	    }
	    
	    @DeleteMapping
	    public void removeFavorite(@RequestParam Integer userId, @RequestParam Integer movieId){

	        favoriteRepository.deleteByUserIdAndMovieId(userId,movieId);
	    }

	    @GetMapping("/check")
	    public boolean isFavorite( @RequestParam Integer userId, @RequestParam Integer movieId){

	        return favoriteRepository.existsByUserIdAndMovieId(userId,movieId);
	    }

}
