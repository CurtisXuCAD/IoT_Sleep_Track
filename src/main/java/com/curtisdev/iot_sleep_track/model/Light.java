package com.curtisdev.iot_sleep_track.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Light {
    private int id;
    private int light_id;
    private String light_on_time;
    private String light_off_time;
    private DateTimeFormatter sql_style_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter local_date_time_formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Light(int id, int light_id, String light_on_time, String light_off_time) {
        this.id = id;
        this.light_id = light_id;

        if (light_on_time != null) {
            LocalDateTime start_time = LocalDateTime.parse(light_on_time, this.sql_style_formatter);
            this.light_on_time = start_time.format(this.local_date_time_formatter);
        } else {
            this.light_on_time = light_on_time;
        }

        if (light_off_time != null) {
            LocalDateTime end_time = LocalDateTime.parse(light_off_time, this.sql_style_formatter);
            this.light_off_time = end_time.format(this.local_date_time_formatter);
        } else {
            this.light_off_time = light_off_time;
        }
    }

    public int getLight_id() {
        return light_id;
    }

    public void setLight_id(int light_id) {
        this.light_id = light_id;
    }

    public String getLight_on_time() {
        return light_on_time;
    }

    public void setLight_on_time(String light_on_time) {
        this.light_on_time = light_on_time;
    }

    public String getLight_off_time() {
        return light_off_time;
    }

    public void setLight_off_time(String light_off_time) {
        this.light_off_time = light_off_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Light{" + "id=" + id + ", light_id=" + light_id + ", light_on_time=" + light_on_time + ", light_off_time=" + light_off_time + '}';
    }
}
