-- Seeds five sentiment-ready reviews for every movie currently present in the movies table.
-- Run this after Spring Boot has started once so Hibernate can add the sentiment_label column.
-- Re-running this script is safe because it skips movies that already have reviews.

INSERT INTO reviews (user_id, movie_id, review_text, sentiment_score, sentiment_label)
SELECT 1, m.id, CONCAT(m.title, ' is brilliant, engaging, and absolutely worth watching.'), 0.75, 'Positive'
FROM movies m
WHERE NOT EXISTS (SELECT 1 FROM reviews r WHERE r.movie_id = m.id)
UNION ALL
SELECT 2, m.id, CONCAT('I loved the story and performances in ', m.title, '.'), 0.75, 'Positive'
FROM movies m
WHERE NOT EXISTS (SELECT 1 FROM reviews r WHERE r.movie_id = m.id)
UNION ALL
SELECT 3, m.id, CONCAT(m.title, ' has a solid pace and some good moments.'), 0.38, 'Positive'
FROM movies m
WHERE NOT EXISTS (SELECT 1 FROM reviews r WHERE r.movie_id = m.id)
UNION ALL
SELECT 4, m.id, CONCAT(m.title, ' was okay overall, but a few scenes felt slow.'), -0.13, 'Neutral'
FROM movies m
WHERE NOT EXISTS (SELECT 1 FROM reviews r WHERE r.movie_id = m.id)
UNION ALL
SELECT 5, m.id, CONCAT(m.title, ' was disappointing and predictable in parts.'), -0.63, 'Negative'
FROM movies m
WHERE NOT EXISTS (SELECT 1 FROM reviews r WHERE r.movie_id = m.id);
