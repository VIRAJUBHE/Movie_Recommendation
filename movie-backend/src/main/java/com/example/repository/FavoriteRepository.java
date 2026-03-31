package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Favourite;

public interface FavoriteRepository extends JpaRepository<Favourite,Integer>{

List<Favourite> findByUserId(Integer userId);

void deleteByUserIdAndMovieId(Integer userId, Integer movieId);

boolean existsByUserIdAndMovieId(Integer userId, Integer movieId);

}