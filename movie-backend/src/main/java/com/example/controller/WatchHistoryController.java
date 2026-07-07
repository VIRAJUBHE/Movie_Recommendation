package com.example.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Movie;
import com.example.entity.WatchHistory;
import com.example.repository.WatchHistoryRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/watch-history")
@CrossOrigin(origins = "http://localhost:4200")
public class WatchHistoryController {
	
	@Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @PostMapping
    @Transactional
    public WatchHistory saveWatchHistory(
            @RequestBody WatchHistory history) {

        watchHistoryRepository.deleteByUserIdAndMovieId(
                history.getUserId(),
                history.getMovieId());

        history.setWatchedAt(LocalDateTime.now());

        return watchHistoryRepository.save(history);
    }
    
    @GetMapping("/{userId}")
    public List<Movie> getRecentlyWatched(@PathVariable Integer userId) {
        return watchHistoryRepository.findRecentlyWatched(userId);
    }

}
