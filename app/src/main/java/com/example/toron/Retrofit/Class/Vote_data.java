package com.example.toron.Retrofit.Class;

public class Vote_data {
    String vote_subject;
    String vote_description;

    public String getVote_subject() {
        return vote_subject;
    }

    public void setVote_subject(String vote_subject) {
        this.vote_subject = vote_subject;
    }

    public String getVote_description() {
        return vote_description;
    }

    public void setVote_description(String vote_description) {
        this.vote_description = vote_description;
    }

    public Vote_data(String vote_subject, String vote_description) {
        this.vote_subject = vote_subject;
        this.vote_description = vote_description;
    }

    @Override
    public String toString() {
        return "Vote_data{" +
                "vote_subject='" + vote_subject + '\'' +
                ", vote_description='" + vote_description + '\'' +
                '}';
    }
}
