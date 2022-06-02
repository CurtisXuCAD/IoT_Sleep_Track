package com.curtisdev.iot_sleep_track.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sleep {
    private int id;
    private int user_id;
    private String sleep_start;
    private String sleep_end;
    private DateTimeFormatter sql_style_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter local_date_time_formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Sleep(int id, int user_id, String sleep_start, String sleep_end) {
        this.id = id;
        this.user_id = user_id;

        if (sleep_start != null) {
            LocalDateTime start_time = LocalDateTime.parse(sleep_start, this.sql_style_formatter);
            this.sleep_start = start_time.format(this.local_date_time_formatter);
        } else {
            this.sleep_start = sleep_start;
        }

        if (sleep_end != null) {
            LocalDateTime end_time = LocalDateTime.parse(sleep_end, this.sql_style_formatter);
            this.sleep_end = end_time.format(this.local_date_time_formatter);
        } else {
            this.sleep_end = sleep_end;
        }
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSleep_start() {
        return sleep_start;
    }

    public void setSleep_start(String sleep_start) {
        this.sleep_start = sleep_start;
    }

    public String getSleep_end() {
        return sleep_end;
    }

    public void setSleep_end(String sleep_end) {
        this.sleep_end = sleep_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Sleep{" + "id=" + id + ", user_id=" + user_id + ", sleep_start=" + sleep_start + ", sleep_end=" + sleep_end + '}';
    }
}
