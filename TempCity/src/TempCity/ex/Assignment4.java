package TempCity.ex;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Assignment4 {
    private static final String GEOCODING_API_KEY = "YOUR_GEOCODING_API_KEY";
    private static final String WEATHER_API_KEY = "YOUR_WEATHER_API_KEY";
    private static final String GEOCODING_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=imperial";

    // return a GeoInfo object for the given city
    public static GeoInfo getGeoInfo(String city) {
        try {
            String apiUrl = String.format(GEOCODING_API_URL, city, GEOCODING_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                return gson.fromJson(response.toString(), GeoInfo.class);
            } else {
                System.err.println("Failed to fetch geocoding data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // return temperature for the given GeoInfo object
    public static double getTemperature(GeoInfo gi) {
        try {
            String apiUrl = String.format(WEATHER_API_URL, gi.lat, gi.lon, WEATHER_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                WeatherInfo weatherInfo = gson.fromJson(response.toString(), WeatherInfo.class);
                return weatherInfo.main.temp;
            } else {
                System.err.println("Failed to fetch weather data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Double.NaN;
    }
//
    public static void main(String[] args) {
        GeoInfo gi = getGeoInfo("Savannah");
        if (gi != null) {
            System.out.println("lat = " + gi.lat + " lon = " + gi.lon);
            double temp = getTemperature(gi);
            if (!Double.isNaN(temp)) {
                System.out.printf("Temperature: %3.1f (Fahrenheit)\n", temp);
            } else {
                System.err.println("Failed to get temperature");
            }
        } else {
            System.err.println("Failed to get geocoding data");
        }
    }
//
    class GeoInfo {
        String name;
        double lat;
        double lon;
    }

    class WeatherInfo {
        MainInfo main;
    }

    class MainInfo {
        double temp;
    }
}
