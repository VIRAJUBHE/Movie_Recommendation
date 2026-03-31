package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "watch_history")
public class WatchHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer movieId;

    private LocalDateTime watchedAt;

	public WatchHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public LocalDateTime getWatchedAt() {
		return watchedAt;
	}

	public void setWatchedAt(LocalDateTime watchedAt) {
		this.watchedAt = watchedAt;
	}
    
    

}
