package com.curtisdev.iot_sleep_track.controller;

import com.curtisdev.iot_sleep_track.mapper.LightMapper;
import com.curtisdev.iot_sleep_track.model.Day_Duration;
import com.curtisdev.iot_sleep_track.model.Light;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/light")
public class LightController {

    private final LightMapper lightMapper;

    public LightController(LightMapper lightMapper) {
        this.lightMapper = lightMapper;
    }

    @RequestMapping("/api")
    @ResponseBody
    public String post_light_state(@RequestParam(value = "state") String state) {
        //check db valid
        Light last_light = lightMapper.get_last_light(1);

        if (state.equals("on")) {
            if (last_light != null && last_light.getLight_off_time() == null) {
                return "error: its already on\n" + last_light.toString();
            }
            lightMapper.light_turn_on(1);
        } else if (state.equals("off")) {
            if (last_light == null || last_light.getLight_off_time() != null) {
                return "error: its already off\n" + last_light.toString();
            }
            lightMapper.light_turn_off(1);
        } else {
            return "error: invalid state";
        }

        return "success: " + lightMapper.get_last_light(1);  //return last sleep
    }

    @GetMapping("/pull")
    @ResponseBody
    public String get_light_state() {
        List<Light> light_list = lightMapper.get_lights_data(1);
        JSONArray result = new JSONArray();

        for (Light light : light_list) {
            JSONObject item = new JSONObject();
            item.put("id", light.getId());
            item.put("light_on_time", light.getLight_on_time());
            item.put("light_off_time", light.getLight_off_time());
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
        List<Light> light_list = lightMapper.get_all_light_data();
        JSONArray result = new JSONArray();

        for (Light light : light_list) {
            JSONObject item = new JSONObject();
            item.put("id", light.getId());
            item.put("light_id", light.getLight_id());
            item.put("light_on_time", light.getLight_on_time());
            item.put("light_off_time", light.getLight_off_time());
            result.add(item);
        }

        if(result.size() == 0) {
            return "error: no data";
        }

        return result.toString();
    }

    @GetMapping("/display/light_usage")
    @ResponseBody
    public String get_sleep_duration_distribution() {
        List<Day_Duration> day_durations = lightMapper.get_light_usage_table();
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
