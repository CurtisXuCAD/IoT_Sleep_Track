package com.curtisdev.iot_sleep_track.controller;

import com.curtisdev.iot_sleep_track.mapper.SleepMapper;
import com.curtisdev.iot_sleep_track.model.Day_Duration;
import com.curtisdev.iot_sleep_track.model.Hour_Count;
import com.curtisdev.iot_sleep_track.model.Light;
import com.curtisdev.iot_sleep_track.model.Sleep;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sleep")
public class SleepController {
    private final SleepMapper sleepMapper;
    private DateTimeFormatter sql_style_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter instant_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public SleepController(SleepMapper sleepMapper) {
        this.sleepMapper = sleepMapper;
    }

    @RequestMapping("/api")
    @ResponseBody
    public String post_sleep_state(@RequestParam(value = "state") String state) {
        //check db valid
        Sleep last_sleep = sleepMapper.get_last_sleep(1);
        String msg = "";

        if (state.equals("start")) {
            if (last_sleep != null && last_sleep.getSleep_end() == null) {
                sleepMapper.delete_last_none_end_sleep(1);
                msg += "\nalready sleep! delete last sleep data";
            }
            sleepMapper.sleep_start(1);
        } else if (state.equals("end")) {
            if (last_sleep == null || last_sleep.getSleep_end() != null) {
                return "error: its already off\n" + last_sleep.toString();
            }
            sleepMapper.sleep_end(1);
        } else {
            return "error: invalid state";
        }

        return "success: " + sleepMapper.get_last_sleep(1).toString() + msg;  //return last sleep
    }

    @GetMapping("/pull")
    @ResponseBody
    public String get_sleep_state() {
        List<Sleep> sleep_list = sleepMapper.get_sleeps_data(1);
        JSONArray result = new JSONArray();

        for (Sleep sleep : sleep_list) {
            JSONObject item = new JSONObject();
            item.put("id", sleep.getId());
            item.put("sleep_start", sleep.getSleep_start());
            item.put("sleep_end", sleep.getSleep_end());
            result.add(item);
        }

        if(result.size() == 0) {
            return "error: no data";
        }

        return result.toString();
    }

    @GetMapping("/pull_all")
    @ResponseBody
    public String get_all_data() {
        List<Sleep> sleep_list = sleepMapper.get_all_sleep_data();
        JSONArray result = new JSONArray();

        for (Sleep sleep : sleep_list) {
            JSONObject item = new JSONObject();
            item.put("id", sleep.getId());
            item.put("user_id", sleep.getUser_id());
            item.put("sleep_start", sleep.getSleep_start());
            item.put("sleep_end", sleep.getSleep_end());
            result.add(item);
        }

        if(result.size() == 0) {
            return "error: no data";
        }

        return result.toString();
    }

    @GetMapping("/upload")
    @ResponseBody
    public String upload_data(@RequestParam(value = "start_time") String start_time, @RequestParam(value = "end_time") String end_time) {

        LocalDateTime sleep_time_input = LocalDateTime.parse(start_time, this.instant_formatter);
        String sleep_time_sql = sleep_time_input.format(this.sql_style_formatter);

        LocalDateTime wake_time_input = LocalDateTime.parse(end_time, this.instant_formatter);
        String wake_time_sql = wake_time_input.format(this.sql_style_formatter);

        sleepMapper.insert_sleep(1, sleep_time_sql, wake_time_sql);
        return "success";
    }

    @GetMapping("/display/wake_time")
    @ResponseBody
    public String get_wake_time_distribution() {
        List<Hour_Count> hour_counts = sleepMapper.get_hour_count_table();

        ArrayList<Integer> hour_count_list = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            hour_count_list.add(0);
        }

        for(Hour_Count hour_count : hour_counts) {
            hour_count_list.set(hour_count.getHour(), hour_count.getCount());
        }

        JSONObject result = new JSONObject();
        result.put("hour_count", hour_count_list);
        result.put("total_count", hour_count_list.stream().mapToInt(Integer::intValue).sum());

        return result.toString();
    }

    @GetMapping("/display/sleep_duration")
    @ResponseBody
    public String get_sleep_duration_distribution() {
        List<Day_Duration> day_durations = sleepMapper.get_day_duration_table();
        ArrayList<String> day_list = new ArrayList<>();
        ArrayList<Integer> duration_list = new ArrayList<>();

        for(Day_Duration day_duration : day_durations) {
            day_list.add(day_duration.getDay());
            duration_list.add(day_duration.getDuration());
        }

        JSONObject result = new JSONObject();
        result.put("date", day_list);
        result.put("value", duration_list);

        return result.toString();
    }

}
