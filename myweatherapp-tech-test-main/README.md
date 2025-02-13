# MyWeather App Tech Test

Welcome to the MyWeather App Tech Test.

## The Challenge

You are tasked with implementing two new features in the app:

1. **Daylight Hours Comparison:** Given two city names, compare the length of the daylight hours between them and return the city with the longest day. In this context, "daylight hours" means the time between sunrise and sunset.

2. **Rain Check:** Given two city names, check which city it is currently raining in.

In addition to implementing these 2 features, you will also need to write tests verifying that your code works as expected.

If possible, include exception handling in the controller.

Finally, you can write any documentation as you see fit, explaining your choices and how the code works.

## The Codebase

The codebase is a Java application built with the Spring framework. It includes a `WeatherController` class where you will add your new features.

## Implementation Details

You will need to implement these features by adding new endpoints to the `WeatherController`.

### Prerequisites

- [Java sdk 17](https://openjdk.java.net/projects/jdk/17/)
- [Maven 3.6.3+](https://maven.apache.org/install.html)
- API key for [Visual Crossing Weather API](https://www.visualcrossing.com/weather-data-editions). 
  - This can be done by creating a free account on the above link. Then you will need to add your key to the `weather.visualcrossing.key` field in src/main/resources/application.properties


## Documentaion of App changes
## App feature 1 and implemention of App feature 2 and 3

1. Get Weather Forecast for a City

Endpoint: GET /weather/forecast/{city}

Description: Fetches weather data for the specified city.

Example Request:

`GET /weather/forecast/London`

Example Response:

```
{
    "address": "London, UK",
    "currentConditions": {
        "temp": "10",
        "sunrise": "6:30 AM",
        "sunset": "5:45 PM",
        "conditions": "Partly Cloudy"
    }
}
```

2. Compare Daylight Hours Between Two Cities
Endpoint: GET /weather/compare-daylight/{city1}/{city2}

Description: Compares the length of daylight hours between two cities and returns the city with the longest daylight hours.

Logic:

Fetches sunrise and sunset times for both cities.

Converts them to `LocalTime using DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)`

Computes the daylight duration (sunset - sunrise).

Compares both cities and returns the one with the longest daylight hours.

Example Request:

`GET /weather/compare-daylight/London/New York`

Example Response:

City with the longest daylight hours: London

3. Check Which City is Raining

Endpoint: `GET /weather/rain-check/{city1}/{city2}`

Description: Determines if it is currently raining in one or both cities.

Logic:

Fetches weather conditions for both cities.

Checks if the conditions field contains the word "rain".

Returns which city (or both) is experiencing rain.

Example Request:

`GET /weather/rain-check/London/New York`

Example Response:

London is currently experiencing rain.

## Exception handling within WeatherController

Exception handling is implemented in WeatherController.java:

### Handling Null Responses: If a city is not found, the controller returns:

`return ResponseEntity.badRequest().body("One or both cities could not be found.");`

### Parsing Errors: Ensures that sunrise/sunset times are correctly formatted to prevent DateTimeParseException.

```
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
LocalTime sunrise = LocalTime.parse(cityInfo.currentConditions.sunrise, formatter);
```

## Approach to unit testing

Unit tests are written in WeatherServiceTest.java using JUnit and Mockito.

### Mocking API Calls

-External API calls are mocked using **Mockito** to avoid real network requests.

-Example:

```
when(weatherService.forecastByCity("City1")).thenReturn(cityInfo1);
when(weatherService.forecastByCity("City2")).thenReturn(cityInfo2);
```

### Test Coverage

| Test Case | Description |
|```````````|`````````````|
| testCompareDaylightHours() | checks that daylight hour comparison logic correctly determines the city with longer daylight |
| testCheckRain() | checks that which city is currently experiencing rain is correctly identified |
| testForecastByCity() | checks that the forecast retrieval for each city is working |
| testCompareDaylightHandlesInvalidCity() | checks that daylight comparison method correctly handles exception where a city isnt found |
| testCheckRainWithBothCitiesRaining() | checks that checkRain method correctly handles case where both cities are experiencing rain |
| testCheckRainWithNoRainInCities() | checks that checkRain method correctly handles case where both cities aren't experiencing rain |






