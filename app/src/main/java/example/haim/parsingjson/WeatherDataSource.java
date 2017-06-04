package example.haim.parsingjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DELL e7440 on 01/06/2017.
 * Get Weather Data from OpenWeatherMap API
 */

public class WeatherDataSource {

    //http://api.openweathermap.org/data/2.5/weather?q=Tel-Aviv,IL&appid=288ca3c192923f79bd74f4d01a9299c0&units=metric

    public interface OnWeatherArrivedListener{
        void onWeatherArrived(Weather data, Exception e);
    }

    ////Static Method -> getWeather()
    public static void getWeather(final OnWeatherArrivedListener listener){

            //Thread creation:
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    //code that runs in the background:
                    //URL url = new ...
                    try {
                        URL url = new URL("http://api.openweathermap.org/data/2.5/"+
                                "weather?q=Tel-Aviv,IL&"+
                                "appid=288ca3c192923f79bd74f4d01a9299c0&units=metric");

                        //Connection - con = url.openConnection
                        URLConnection con = url.openConnection();

                        //in = con.inputStream() (BINARY)
                        InputStream in = con.getInputStream();

                        //String json = IO.getString(in); -> IO...
                        String json = IO.getString(in);
                        //Log.d("Hackeru", json);

                        //Weather w = parseJson(json)
                        Weather w = parseJson(json);
                        //Log.d("Hackeru", w.toString());

                        //notify the listener that weather arrived
                        listener.onWeatherArrived(w, null);

                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onWeatherArrived(null, e);
                        //Can't connect to server... please try again later.
                    }
                }
            });


    }

    private static Weather parseJson(String json) throws JSONException {
        JSONObject root  = new JSONObject(json);

        JSONArray weatherArray = root.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String description = weatherObject.getString("description");
        String icon = weatherObject.getString("icon");
        Double temp = root.getJSONObject("main").getDouble("temp");
        JSONObject sys = root.getJSONObject("sys");
        long sunrise = sys.getLong("sunrise");
        long sunset = sys.getLong("sunset");

        return new Weather(description, icon, temp, sunset, sunrise);
    }

    //Weather?

    public static class Weather{
        //Properties:
        private String description;
        private String icon;
        private double temp;
        private long sunset;
        private long sunrise;

        //Constructor:
        public Weather(String description, String icon, double temp, long sunset, long sunrise) {
            this.description = description;
            this.icon = icon;
            this.temp = temp;
            this.sunset = sunset;
            this.sunrise = sunrise;
        }

        //Getters
        public String getDescription() {
            return description;
        }
        public String getIcon() {
            return icon;
        }
        public double getTemp() {
            return temp;
        }
        public long getSunset() {
            return sunset;
        }
        public long getSunrise() {
            return sunrise;
        }

        //toString()
        @Override
        public String toString() {
            return "Weather{" +
                    "description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    ", temp=" + temp +
                    ", sunset=" + sunset +
                    ", sunrise=" + sunrise +
                    '}';
        }

    }



}
