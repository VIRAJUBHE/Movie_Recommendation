package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SentimentService {

    private static final Pattern SCORE_PATTERN = Pattern.compile("\\\"score\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern LABEL_PATTERN = Pattern.compile("\\\"label\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    private final String pythonCommand;
    private final String analyzerPath;

    public SentimentService(
            @Value("${sentiment.python.command:python}") String pythonCommand,
            @Value("${sentiment.python.script:python/sentiment_analyzer.py}") String analyzerPath) {
        this.pythonCommand = pythonCommand;
        this.analyzerPath = analyzerPath;
    }

    public SentimentResult analyze(String text) {
        try {
            Process process = new ProcessBuilder(pythonCommand, analyzerPath, text == null ? "" : text)
                    .redirectErrorStream(true)
                    .start();

            boolean finished = process.waitFor(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
            String output = readOutput(process);

            if (!finished) {
                process.destroyForcibly();
                return new SentimentResult(0.0f, "Neutral");
            }

            if (process.exitValue() != 0) {
                return new SentimentResult(0.0f, "Neutral");
            }

            return parseResult(output);
        } catch (IOException ex) {
            return new SentimentResult(0.0f, "Neutral");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return new SentimentResult(0.0f, "Neutral");
        }
    }

    private SentimentResult parseResult(String output) {
        Matcher scoreMatcher = SCORE_PATTERN.matcher(output);
        Matcher labelMatcher = LABEL_PATTERN.matcher(output);

        float score = scoreMatcher.find() ? Float.parseFloat(scoreMatcher.group(1)) : 0.0f;
        String label = labelMatcher.find() ? labelMatcher.group(1) : "Neutral";

        return new SentimentResult(score, label);
    }

    private String readOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            return output.toString();
        }
    }
}
