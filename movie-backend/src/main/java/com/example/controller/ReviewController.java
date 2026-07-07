package com.example.controller;

import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Review;
import com.example.repository.ReviewRepository;
import com.example.service.SentimentResult;
import com.example.service.SentimentService;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private SentimentService sentimentService;

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        SentimentResult sentiment = sentimentService.analyze(review.getReviewText());
        review.setSentimentScore(sentiment.score());
        review.setSentimentLabel(sentiment.label());

        return reviewRepository.save(review);
    }

    @GetMapping("/movie/{movieId}")
    public List<Review> getReviewsByMovie(@PathVariable Integer movieId) {
        return reviewRepository.findByMovieIdOrderByIdDesc(movieId);
    }

    @GetMapping("/movie/{movieId}/summary")
    public Map<String, Object> getSentimentSummary(@PathVariable Integer movieId) {
        List<Review> reviews = reviewRepository.findByMovieIdOrderByIdDesc(movieId);
        DoubleSummaryStatistics stats = reviews.stream()
                .filter(review -> review.getSentimentScore() != null)
                .mapToDouble(Review::getSentimentScore)
                .summaryStatistics();

        Map<String, Long> distribution = new LinkedHashMap<>();
        distribution.put("Positive", countByLabel(reviews, "Positive"));
        distribution.put("Neutral", countByLabel(reviews, "Neutral"));
        distribution.put("Negative", countByLabel(reviews, "Negative"));

        double averageScore = stats.getCount() == 0 ? 0.0 : stats.getAverage();
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalReviews", reviews.size());
        summary.put("averageScore", averageScore);
        summary.put("overallSentiment", labelForAverage(averageScore));
        summary.put("distribution", distribution);

        return summary;
    }

    private long countByLabel(List<Review> reviews, String label) {
        return reviews.stream()
                .filter(review -> label.equals(review.getSentimentLabel()))
                .count();
    }

    private String labelForAverage(double score) {
        if (score >= 0.2) {
            return "Positive";
        }

        if (score <= -0.2) {
            return "Negative";
        }

        return "Neutral";
    }
}
