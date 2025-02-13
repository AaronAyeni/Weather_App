package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo cityInfo = weatherService.forecastByCity(city);

    return ResponseEntity.ok(cityInfo);
  }

  // TODO: given two city names, compare the length of the daylight hours and return the city with the longest day

  @GetMapping("/compare-daylight/{city1}/{city2}")
  public ResponseEntity<String> compareDaylight(@PathVariable String city1, @PathVariable String city2) {
    CityInfo cityInfo1 = weatherService.forecastByCity(city1);
    CityInfo cityInfo2 = weatherService.forecastByCity(city2);

    if (cityInfo1 == null || cityInfo2 == null) {
      return ResponseEntity.badRequest().body("One or both cities could not be found.");
    }

    LocalTime sunrise1 = LocalTime.parse(cityInfo1.currentConditions.sunrise, DateTimeFormatter.ofPattern("h:mm a"));
    LocalTime sunset1 = LocalTime.parse(cityInfo1.currentConditions.sunset, DateTimeFormatter.ofPattern("h:mm a"));
    long daylight1 = sunset1.toSecondOfDay() - sunrise1.toSecondOfDay();

    LocalTime sunrise2 = LocalTime.parse(cityInfo2.currentConditions.sunrise, DateTimeFormatter.ofPattern("h:mm a"));
    LocalTime sunset2 = LocalTime.parse(cityInfo2.currentConditions.sunset, DateTimeFormatter.ofPattern("h:mm a"));
    long daylight2 = sunset2.toSecondOfDay() - sunrise2.toSecondOfDay();

    String result = (daylight1 > daylight2) ? city1 : city2;
    return ResponseEntity.ok("City with the longest daylight hours: " + result);
  }

  // TODO: given two city names, check which city its currently raining in

  @GetMapping("/rain-check/{city1}/{city2}")
  public ResponseEntity<String> checkRain(@PathVariable String city1, @PathVariable String city2) {
    CityInfo cityInfo1 = weatherService.forecastByCity(city1);
    CityInfo cityInfo2 = weatherService.forecastByCity(city2);

    if (cityInfo1 == null || cityInfo2 == null) {
      return ResponseEntity.badRequest().body("One or both cities could not be found.");
    }

    boolean isRaining1 = cityInfo1.currentConditions.conditions.toLowerCase().contains("rain");
    boolean isRaining2 = cityInfo2.currentConditions.conditions.toLowerCase().contains("rain");

    if (isRaining1 && isRaining2) {
      return ResponseEntity.ok("Both cities are experiencing rain.");
    } else if (isRaining1) {
      return ResponseEntity.ok(city1 + " is currently experiencing rain.");
    } else if (isRaining2) {
      return ResponseEntity.ok(city2 + " is currently experiencing rain.");
    } else {
      return ResponseEntity.ok("Neither city is currently experiencing rain.");
    }
  }

}
