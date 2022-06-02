package com.curtisdev.iot_sleep_track.model;

public class Day_Duration {
    private String day;
    private int duration;

    public Day_Duration(String day, int duration) {
        this.day = day;
        this.duration = duration;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
