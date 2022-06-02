package com.curtisdev.iot_sleep_track.mapper;

import com.curtisdev.iot_sleep_track.model.Light;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InfoMapper {
    @Select("SELECT NOW()")
    String get_current_time();

    @Select("SELECT * FROM lights WHERE light_id = #{light_id} ORDER BY id DESC LIMIT 1")
    Light get_last_light(Integer light_id);

    @Delete("TRUNCATE TABLE lights")
    void clear_data_light();

    @Delete("TRUNCATE TABLE sleeps")
    void clear_data_sleep();

    @Delete("TRUNCATE TABLE sleepiness_datas")
    void clear_data_sleepiness();
}
