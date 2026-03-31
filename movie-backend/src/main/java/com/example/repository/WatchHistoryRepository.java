package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Movie;
import com.example.entity.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer> {
	
	@Query(value = """
			SELECT m.*
			FROM movies m
			JOIN watch_history w
			ON m.id = w.movie_id
			WHERE w.user_id = :userId
			ORDER BY w.watched_at DESC
			LIMIT 10
			""", nativeQuery = true)
			List<Movie> findRecentlyWatched(@Param("userId") Integer userId);
}
