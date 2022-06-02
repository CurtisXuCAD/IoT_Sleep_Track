package com.curtisdev.iot_sleep_track.mapper;

import com.curtisdev.iot_sleep_track.model.Sleepiness;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SleepinessMapper {
    @Insert("INSERT INTO sleepiness_datas(user_id, track_time, sleepiness) VALUES(#{user_id,jdbcType=INTEGER}, #{track_time, jdbcType=TIMESTAMP}, #{sleepiness_value,jdbcType=INTEGER});")
    void insert_sleepiness(Integer user_id, String track_time, Integer sleepiness_value);

    @Select("SELECT * FROM sleepiness_datas WHERE user_id = #{user_id,jdbcType=INTEGER};")
    List<Sleepiness> get_all_sleepiness_data(Integer user_id);
}

