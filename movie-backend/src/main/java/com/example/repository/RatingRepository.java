package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating,Integer>{

    List<Rating> findByMovieId(Integer movieId);

    List<Rating> findByUserId(Integer userId);
    
    @Query(value="""
    		SELECT movie_id
    		FROM ratings
    		WHERE user_id IN (
    		    SELECT user_id
    		    FROM ratings
    		    WHERE movie_id IN (
    		        SELECT movie_id FROM ratings WHERE user_id = :userId
    		    )
    		)
    		GROUP BY movie_id
    		ORDER BY AVG(rating) DESC
    		LIMIT 10
    		""", nativeQuery = true)
    		List<Integer> findRecommendedMovies(Integer userId);

}
