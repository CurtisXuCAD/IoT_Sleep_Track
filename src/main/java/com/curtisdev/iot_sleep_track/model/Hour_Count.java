package com.curtisdev.iot_sleep_track.model;

public class Hour_Count {
    private int hour;
    private int count;

    public Hour_Count(int hour, int count) {
        this.hour = hour;
        this.count = count;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
