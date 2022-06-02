package com.curtisdev.iot_sleep_track.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sleepiness {
    private int id;
    private int user_id;
    private int sleepiness_value;
    private String track_time;
    private DateTimeFormatter sql_style_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter local_date_time_formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Sleepiness(int id, int user_id, String track_time, int sleepiness_value) {
        this.id = id;
        this.user_id = user_id;
        this.sleepiness_value = sleepiness_value;
        this.track_time = track_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSleepiness_value() {
        return sleepiness_value;
    }

    public void setSleepiness_value(int sleepiness_value) {
        this.sleepiness_value = sleepiness_value;
    }

    public String getTrack_time() {
        LocalDateTime track_time_sql = LocalDateTime.parse(track_time, this.sql_style_formatter);
        return track_time_sql.format(this.local_date_time_formatter);
    }

    public void setTrack_time(String track_time) {
        this.track_time = track_time;
    }

    @Override
    public String toString() {
        return "Sleepiness [id=" + id + ", user_id=" + user_id + ", sleepiness_value=" + sleepiness_value + ", track_time=" + getTrack_time() + "]";
    }
}
