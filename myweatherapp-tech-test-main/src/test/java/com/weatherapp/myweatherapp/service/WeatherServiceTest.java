package com.weatherapp.myweatherapp.service;



import com.weatherapp.myweatherapp.controller.WeatherController;
import com.weatherapp.myweatherapp.model.CityInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


// TODO: 12/05/2023 write unit tests

@SpringBootTest
class WeatherServiceTest {
    @Autowired
    private WeatherController weatherController;

    @MockBean
    private WeatherService weatherService;

    private CityInfo cityInfo1;
    private CityInfo cityInfo2;

    @BeforeEach
    void setUp() {
        // Mock CityInfo 1 with manual initialization as no getters or setters present
        cityInfo1 = new CityInfo();
        cityInfo1.currentConditions = new CityInfo.CurrentConditions();
        cityInfo1.currentConditions.sunrise = "06:00 AM";
        cityInfo1.currentConditions.sunset = "06:00 PM";
        cityInfo1.currentConditions.conditions = "Clear";

        // Mock CityInfo 2 with manual initialization
        cityInfo2 = new CityInfo();
        cityInfo2.currentConditions = new CityInfo.CurrentConditions();
        cityInfo2.currentConditions.sunrise = "07:00 AM";
        cityInfo2.currentConditions.sunset = "07:30 PM";
        cityInfo2.currentConditions.conditions = "Rain";

        // Mock the service to return the mocked city info
        when(weatherService.forecastByCity("City1")).thenReturn(cityInfo1);
        when(weatherService.forecastByCity("City2")).thenReturn(cityInfo2);
    }

    @Test
    void testCompareDaylightHours() {
        // Simulate the controller calling the service
        ResponseEntity<String> response = weatherController.compareDaylight("City1", "City2");

        // City2 should have the longest daylight hours
        assertEquals("City with the longest daylight hours: City2", response.getBody());
    }

    @Test
    void testCompareDaylightHoursWithInvalidCity() {
        // Simulate the scenario where one of the cities is not found
        when(weatherService.forecastByCity("InvalidCity")).thenReturn(null);

        ResponseEntity<String> response = weatherController.compareDaylight("InvalidCity", "City2");
        assertEquals("One or both cities could not be found.", response.getBody());
    }

    @Test
    void testCheckRain() {
        // Simulate the controller calling the service
        ResponseEntity<String> response = weatherController.checkRain("City1", "City2");

        // City2 is currently experiencing rain
        assertEquals("City2 is currently experiencing rain.", response.getBody());
    }

    @Test
    void testCheckRainWithBothCitiesRaining() {
        // Update cityInfo1 to simulate rain
        cityInfo1.currentConditions.conditions = "Rain";
        when(weatherService.forecastByCity("City1")).thenReturn(cityInfo1);

        // Simulate the controller calling the service
        ResponseEntity<String> response = weatherController.checkRain("City1", "City2");

        // Both cities are currently experiencing rain
        assertEquals("Both cities are experiencing rain.", response.getBody());
    }

    @Test
    void testCheckRainWithNoRainInCities() {
        // Update cityInfo2 to simulate clear weather
        cityInfo2.currentConditions.conditions = "Clear";
        when(weatherService.forecastByCity("City2")).thenReturn(cityInfo2);

        // Simulate the controller calling the service
        ResponseEntity<String> response = weatherController.checkRain("City1", "City2");

        // Neither city is currently experiencing rain
        assertEquals("Neither city is currently experiencing rain.", response.getBody());
    }

    @Test
    void testCheckRainWithInvalidCity() {
        // Simulate the scenario where one of the cities is not found
        when(weatherService.forecastByCity("InvalidCity")).thenReturn(null);

        ResponseEntity<String> response = weatherController.checkRain("InvalidCity", "City2");
        assertEquals("One or both cities could not be found.", response.getBody());
    }








}