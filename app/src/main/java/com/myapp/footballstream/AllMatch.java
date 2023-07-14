package com.myapp.footballstream;

public class AllMatch {
    private final String textStatus, homeTeam,  awayTeam, homeImage, awayImage, homeResult, awayResult;
    public AllMatch(String textStatus, String homeTeam, String awayTeam, String homeImage, String awayImage, String homeResult, String awayResult) {
        this.textStatus = textStatus;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeImage = homeImage;
        this.awayImage = awayImage;
        this.homeResult = homeResult;
        this.awayResult = awayResult;
    }
    public String getTextStatus() {
        return textStatus;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }
    public String getHomeImage() {
        return homeImage;
    }
    public String getAwayImage() {
        return awayImage;
    }

    public String getHomeResult() {
        return homeResult;
    }

    public String getAwayResult() {
        return awayResult;
    }
}