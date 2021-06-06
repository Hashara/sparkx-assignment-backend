package com.sparkx.model.dao;

public class StatsDAO {
    private int newCases;
    private int recovered;
    private int deaths;

    public int getNewCases() {
        return newCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public String toString() {
        return "newCases: " + newCases + ", recovered:" + recovered + ", deaths:" + deaths;
    }
}
