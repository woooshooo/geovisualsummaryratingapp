package com.thesis2.genise_villanueva.thesis;

public class SentimentInfo {
    Integer reviewcount;
    Double subjectivityscoreaverage;
    Integer positive;
    Integer positiveGTAvg;
    Integer positiveLTAvg;
    Integer negative;
    Integer negativeGTAvg;
    Integer negativeLTAvg;
    Integer neutral;
    Integer neutralGTAvg;
    Integer neutralLTAvg;

    SentimentInfo(){}

    SentimentInfo(Integer reviewcount, Double subjectivityscoreaverage, Integer positive, Integer positiveGTAvg, Integer positiveLTAvg, Integer negative, Integer negativeGTAvg, Integer negativeLTAvg, Integer neutral, Integer neutralGTAvg, Integer neutralLTAvg) {
        this.reviewcount = reviewcount;
        this.subjectivityscoreaverage = subjectivityscoreaverage;
        this.positive = positive;
        this.positiveGTAvg = positiveGTAvg;
        this.positiveLTAvg = positiveLTAvg;
        this.negative = negative;
        this.negativeGTAvg = negativeGTAvg;
        this.negativeLTAvg = negativeLTAvg;
        this.neutral = neutral;
        this.neutralGTAvg = neutralGTAvg;
        this.neutralLTAvg = neutralLTAvg;
    }

    public Integer getReviewcount() {
        return reviewcount;
    }

    public void setReviewcount(Integer reviewcount) {
        this.reviewcount = reviewcount;
    }

    public Double getSubjectivityscoreaverage() {
        return subjectivityscoreaverage;
    }

    public void setSubjectivityscoreaverage(Double subjectivityscoreaverage) {
        this.subjectivityscoreaverage = subjectivityscoreaverage;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getPositiveGTAvg() {
        return positiveGTAvg;
    }

    public void setPositiveGTAvg(Integer positiveGTAvg) {
        this.positiveGTAvg = positiveGTAvg;
    }

    public Integer getPositiveLTAvg() {
        return positiveLTAvg;
    }

    public void setPositiveLTAvg(Integer positiveLTAvg) {
        this.positiveLTAvg = positiveLTAvg;
    }

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }

    public Integer getNegativeGTAvg() {
        return negativeGTAvg;
    }

    public void setNegativeGTAvg(Integer negativeGTAvg) {
        this.negativeGTAvg = negativeGTAvg;
    }

    public Integer getNegativeLTAvg() {
        return negativeLTAvg;
    }

    public void setNegativeLTAvg(Integer negativeLTAvg) {
        this.negativeLTAvg = negativeLTAvg;
    }

    public Integer getNeutral() {
        return neutral;
    }

    public void setNeutral(Integer neutral) {
        this.neutral = neutral;
    }

    public Integer getNeutralGTAvg() {
        return neutralGTAvg;
    }

    public void setNeutralGTAvg(Integer neutralGTAvg) {
        this.neutralGTAvg = neutralGTAvg;
    }

    public Integer getNeutralLTAvg() {
        return neutralLTAvg;
    }

    public void setNeutralLTAvg(Integer neutralLTAvg) {
        this.neutralLTAvg = neutralLTAvg;
    }
}
