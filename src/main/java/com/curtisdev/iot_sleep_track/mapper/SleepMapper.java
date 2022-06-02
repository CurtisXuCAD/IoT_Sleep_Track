package com.curtisdev.iot_sleep_track.mapper;

import com.curtisdev.iot_sleep_track.model.Day_Duration;
import com.curtisdev.iot_sleep_track.model.Hour_Count;
import com.curtisdev.iot_sleep_track.model.Sleep;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SleepMapper {
    @Insert("INSERT INTO sleeps(user_id, sleep_time, wake_time) VALUES(#{user_id,jdbcType=INTEGER}, NOW(), NULL);")
    void sleep_start(Integer user_id);

    @Insert("UPDATE sleeps SET wake_time = NOW() WHERE user_id = #{user_id,jdbcType=INTEGER} AND wake_time IS NULL;")
    void sleep_end(Integer user_id);

    @Select("SELECT * FROM sleeps WHERE user_id = #{user_id,jdbcType=INTEGER} AND wake_time IS NOT NULL;")
    List<Sleep> get_sleeps_data(Integer user_id);

    @Select("SELECT * FROM sleeps WHERE user_id = #{user_id,jdbcType=INTEGER} ORDER BY id DESC LIMIT 1;")
    Sleep get_last_sleep(Integer user_id);

    @Delete("DELETE FROM sleeps WHERE user_id = #{user_id,jdbcType=INTEGER} AND wake_time IS NULL ORDER BY id DESC LIMIT 1;")
    void delete_last_none_end_sleep(Integer user_id);

    @Select("SELECT * FROM sleeps;")
    List<Sleep> get_all_sleep_data();

    @Insert("INSERT INTO sleeps(user_id, sleep_time, wake_time) VALUES(#{user_id,jdbcType=INTEGER}, #{sleep_time,jdbcType=TIMESTAMP}, #{wake_time,jdbcType=TIMESTAMP});")
    void insert_sleep(Integer user_id, String sleep_time, String wake_time);

    @Select("SELECT HOUR(wake_time) as wake_hour, COUNT(*) as num FROM sleeps where wake_time is not null GROUP BY wake_hour;")
    List<Hour_Count> get_hour_count_table();

    @Select("SELECT DATE(sleep_time), SUM(TIMESTAMPDIFF(HOUR, sleep_time, wake_time)) as diff_sum FROM sleeps where wake_time is not null GROUP BY DATE(sleep_time) ORDER BY DATE(sleep_time) DESC LIMIT 7;")
    List<Day_Duration> get_day_duration_table();
}
