package com.curtisdev.iot_sleep_track.controller;

import com.curtisdev.iot_sleep_track.mapper.SleepinessMapper;
import com.curtisdev.iot_sleep_track.model.Sleepiness;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/sleepiness")
public class SleepinessController {
    private final SleepinessMapper sleepinessMapper;
    private DateTimeFormatter sql_style_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter instant_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public SleepinessController(SleepinessMapper sleepinessMapper) {
        this.sleepinessMapper = sleepinessMapper;
    }

    @GetMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("track_time") String track_time, @RequestParam("sleepiness_value") Integer sleepiness_value) {
        LocalDateTime track_time_input = LocalDateTime.parse(track_time, this.instant_formatter);
        String track_time_sql = track_time_input.format(this.sql_style_formatter);

        sleepinessMapper.insert_sleepiness(1, track_time_sql, sleepiness_value);
        return "upload success";
    }

    @RequestMapping("/pull")
    public String get() {
        List<Sleepiness> sleepinesses = sleepinessMapper.get_all_sleepiness_data(1);
        JSONArray result = new JSONArray();

        for (Sleepiness sleepiness : sleepinesses) {
            JSONObject item = new JSONObject();
            item.put("track_time", sleepiness.getTrack_time());
            item.put("sleepiness_value", sleepiness.getSleepiness_value());
            item.put("id", sleepiness.getId());
            result.add(item);
        }

        if (result.size() == 0) {
            return "error: no data";
        }

        return result.toString();

    }
}
