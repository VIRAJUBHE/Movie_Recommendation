import json
import re
import sys

SENTIMENT_WORDS = {
    "amazing": 3,
    "awesome": 3,
    "beautiful": 2,
    "best": 3,
    "brilliant": 3,
    "excellent": 3,
    "fantastic": 3,
    "favorite": 2,
    "fun": 2,
    "good": 2,
    "great": 3,
    "impressive": 2,
    "love": 3,
    "loved": 3,
    "masterpiece": 4,
    "perfect": 3,
    "powerful": 2,
    "recommend": 2,
    "solid": 1,
    "stunning": 3,
    "superb": 3,
    "terrific": 3,
    "wonderful": 3,
    "bad": -2,
    "boring": -3,
    "confusing": -2,
    "disappointing": -3,
    "dull": -2,
    "flat": -1,
    "hate": -3,
    "hated": -3,
    "messy": -2,
    "poor": -2,
    "predictable": -2,
    "slow": -1,
    "terrible": -3,
    "trash": -4,
    "underwhelming": -2,
    "weak": -2,
    "worst": -4,
}

NEGATIONS = {"not", "never", "no", "hardly", "barely"}


def sentiment_label(score: float) -> str:
    if score >= 0.2:
        return "Positive"
    if score <= -0.2:
        return "Negative"
    return "Neutral"


def analyze(text: str) -> dict:
    if not text or not text.strip():
        return {"score": 0.0, "label": "Neutral"}

    tokens = re.sub(r"[^a-z\s]", " ", text.lower()).split()
    total = 0
    matched_words = 0
    negate_next_sentiment = False

    for token in tokens:
        if token in NEGATIONS:
            negate_next_sentiment = True
            continue

        if token not in SENTIMENT_WORDS:
            continue

        value = SENTIMENT_WORDS[token]
        total += -value if negate_next_sentiment else value
        matched_words += 1
        negate_next_sentiment = False

    if matched_words == 0:
        return {"score": 0.0, "label": "Neutral"}

    score = max(-1.0, min(1.0, total / (matched_words * 4.0)))
    return {"score": round(score, 4), "label": sentiment_label(score)}


def main() -> None:
    text = sys.argv[1] if len(sys.argv) > 1 else sys.stdin.read()
    print(json.dumps(analyze(text)))


if __name__ == "__main__":
    main()
