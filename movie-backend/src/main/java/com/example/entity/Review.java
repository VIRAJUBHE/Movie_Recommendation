package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer movieId;

    private String reviewText;

    private Float sentimentScore;

}
