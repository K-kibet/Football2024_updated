package com.myapp.footballstream;
public class League {
    private final String leagueImage;
    private final String leagueName;
    private final String leagueLocation;
    private final String leagueId;
    private final String leagueSeason;

    public League(String image, String location, String name, String season, String id) {
        this.leagueImage = image;
        this.leagueName = name;
        this.leagueLocation = location;
        this.leagueSeason = season;
        this.leagueId = id;
    }

    public String getLeagueImage() {
        return leagueImage;
    }
    public String getLeagueName() {
        return leagueName;
    }
    public String getLeagueLocation() {
        return leagueLocation;
    }
    public String getLeagueSeason() {
        return leagueSeason;
    }
    public String getLeagueId() {
        return leagueId;
    }
}
