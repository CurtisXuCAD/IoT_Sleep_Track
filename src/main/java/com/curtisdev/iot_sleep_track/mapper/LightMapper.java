package com.curtisdev.iot_sleep_track.mapper;

import com.curtisdev.iot_sleep_track.model.Day_Duration;
import com.curtisdev.iot_sleep_track.model.Light;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LightMapper {
    @Insert("INSERT INTO lights(light_id, light_on, light_off) VALUES(#{light_id,jdbcType=INTEGER}, NOW(), NULL);")
    void light_turn_on(Integer light_id);

    @Insert("UPDATE lights SET light_off = NOW() WHERE light_id = #{light_id,jdbcType=INTEGER} AND light_off IS NULL;")
    void light_turn_off(Integer light_id);

    @Select("SELECT * FROM lights WHERE light_id = #{light_id,jdbcType=INTEGER} AND light_off IS NOT NULL;")
    List<Light> get_lights_data(Integer light_id);

    @Select("SELECT * FROM lights WHERE light_id = #{light_id,jdbcType=INTEGER} ORDER BY id DESC LIMIT 1;")
    Light get_last_light(Integer light_id);

    @Delete("DELETE FROM lights WHERE light_id = #{light_id,jdbcType=INTEGER} AND light_off IS NULL ORDER BY id DESC LIMIT 1;")
    void delete_last_none_end_light(Integer light_id);

    @Select("SELECT * FROM lights;")
    List<Light> get_all_light_data();

    @Select("SELECT DATE(light_on), SUM(TIMESTAMPDIFF(SECOND, light_on, light_off)) as diff_sum FROM lights where light_off is not null GROUP BY DATE(light_on) ORDER BY DATE(light_on) DESC LIMIT 7;")
    List<Day_Duration> get_light_usage_table();
}
