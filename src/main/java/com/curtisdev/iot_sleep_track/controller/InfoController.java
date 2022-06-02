package com.curtisdev.iot_sleep_track.controller;

import com.curtisdev.iot_sleep_track.mapper.InfoMapper;
import com.curtisdev.iot_sleep_track.model.Light;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class InfoController {

    private final InfoMapper infoMapper;

    public InfoController(InfoMapper infoMapper) {
        this.infoMapper = infoMapper;
    }

    @RequestMapping("/info")
    public String info() {
        String current_time = infoMapper.get_current_time();
        Light last_light = infoMapper.get_last_light(1);
        Integer light_is_on;
        if (last_light == null) {
            light_is_on = 0;
        } else {
            if (last_light.getLight_off_time() == null || last_light.getLight_off_time().equals("NULL")) {
                light_is_on = 1;
            } else {
                light_is_on = 0;
            }
        }

        JSONObject item = new JSONObject();
        item.put("current_time", current_time);
        item.put("light_is_on", light_is_on);
        return item.toString();
    }

    @RequestMapping("/info/light")
    public String info_light() {
        Light last_light = infoMapper.get_last_light(1);
        Integer light_is_on;
        if (last_light == null) {
            light_is_on = 0;
        } else {
            if (last_light.getLight_off_time() == null || last_light.getLight_off_time().equals("NULL")) {
                light_is_on = 1;
            } else {
                light_is_on = 0;
            }
        }
        return light_is_on.toString();
    }

    @RequestMapping("/info/current_time")
    public String info_current_time() {
        String current_time = infoMapper.get_current_time();
        return current_time;
    }

    @RequestMapping("/admin/clear_data/light")
    public String clear_light_data() {
        infoMapper.clear_data_light();
        return "success clear light data";
    }

    @RequestMapping("/admin/clear_data/sleep")
    public String clear_sleep_data() {
        infoMapper.clear_data_sleep();
        return "success clear sleep data";
    }

    @RequestMapping("/admin/clear_data/sleepiness")
    public String clear_sleepiness_data() {
        infoMapper.clear_data_sleepiness();
        return "success clear sleepiness data";
    }

    @RequestMapping("/admin/clear_data/all")
    public String clear_data() {
        infoMapper.clear_data_light();
        infoMapper.clear_data_sleep();
        infoMapper.clear_data_sleepiness();
        return "success clear all data";
    }

    @RequestMapping("/info/is_night")
    public String is_night() {
        String current_time = infoMapper.get_current_time();
        String[] time = current_time.split(" ");
        String[] time_split = time[1].split(":");
        Integer hour = Integer.parseInt(time_split[0]);
        if (hour >= 22 || hour <= 6) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping("/info/weather")
    @ResponseBody
    public String weather() {
        String url = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/337095?apikey=" + "EOjhT9tR1fCV5lFa3hRTjzw86GUFoW0a";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(get);
            HttpEntity entity = resp.getEntity();
            System.out.println("Json response");
            String response = EntityUtils.toString(entity);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject day_info = jsonObject.getJSONArray("DailyForecasts").getJSONObject(0);
                //date
                String date = day_info.getString("Date");
                DateTimeFormatter source_formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                DateTimeFormatter target_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dt = LocalDateTime.parse(date, source_formatter);
                String date_str = dt.format(target_formatter);



                Double min_temp = day_info.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value");
                Double max_temp = day_info.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value");
                String weather_text = day_info.getJSONObject("Day").getString("IconPhrase");

                String weather_info = "Min Temperature: " + String.format("%.1f", ((min_temp-32)*5)/9) + " °C\nMax Temperature: " + String.format("%.1f", ((max_temp-32)*5)/9) + " °C\nCondition: " + weather_text;

                String recommendation_msg = "Wear ";
                if (min_temp < 5 && max_temp < 10) {
                    recommendation_msg += "a thick jumper";
                } else if (min_temp > 20 && max_temp > 20) {
                    recommendation_msg += "shorts";
                } else if (min_temp < 20 && max_temp > 20) {
                    recommendation_msg += "shorts and a jumper";
                } else {
                    recommendation_msg += "something not too thin and not too thick";
                }

                if(weather_text.contains("Rain") || weather_text.contains("Showers") || weather_text.contains("Storms") || weather_text.contains("Snow")) {
                    recommendation_msg += " and put on your rain jacket";
                }

                String weather_info_str = "Here's the weather on" +
                        " " + date_str + "\n" +
                        weather_info;

                String result = "The weather in Irvine:\n" +
                        weather_info_str + "\n\n" +
                        "RECOMMENDATION:\n"+
                        recommendation_msg;

                System.out.println(result);

                JSONObject item = new JSONObject();
                item.put("weather_info_str", weather_info_str);
                item.put("recommendation_msg", recommendation_msg);

                return item.toString();


            } catch (JSONException err) {
                System.out.println("Exception : "+err.toString());
            }

        }
        catch (IOException ioe) { System.err.println("Something went wrong getting the weather: ");  ioe.printStackTrace(); }
        catch (Exception e ){ System.err.println("Unknown error: "); e.printStackTrace(); }

        return "error";
    }
}
