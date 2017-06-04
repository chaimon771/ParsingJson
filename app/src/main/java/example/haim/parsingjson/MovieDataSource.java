package example.haim.parsingjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DELL e7440 on 01/06/2017.
 */

public class MovieDataSource {
    //http://api.androidhive.info/json/movies.json

    //1) Movie?
    //static class no need reference to the dataSource
    public static class Movie{
        private final String title;
        private final String image;
        private final int releaseYear;
        private final double rating;
        private final String[] genre;

        //Constructor, Getters, toString:
        public Movie(String title, String image, int releaseYear, double rating, String[] genre) {
            this.title = title;
            this.image = image;
            this.releaseYear = releaseYear;
            this.rating = rating;
            this.genre = genre;
        }

        public String getTitle() {
            return title;
        }
        public String getImage() {
            return image;
        }
        public int getReleaseYear() {
            return releaseYear;
        }
        public double getRating() {
            return rating;
        }
        public String[] getGenre() {
            return genre;
        }

        @Override
        public String toString() {
            return "Movie{" +
                    "title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    ", releaseYear=" + releaseYear +
                    ", rating=" + rating +
                    ", genre=" + Arrays.toString(genre) +
                    '}';
        }
    }

    //2) Listener
    public interface OnMoviesArrivedListener{
        void onMoviesArrived(List<Movie> data, Exception e);
    }

    //3) public static method(Listener listener)
    public static void getMovies(final OnMoviesArrivedListener listener){
        //3.1) ExecutorService or Thread
        ExecutorService service = Executors.newSingleThreadExecutor();
        //3.2) Service.execute(Runnable)
        service.execute(new Runnable() {
            @Override
            public void run() {
                //3.2.0) try catch(exception e)
                try{
                    //3.2.1) URL url = new...
                    URL url = new URL("http://api.androidhive.info/json/movies.json");
                    //3.2.2)URLConnection con = url.openConnection
                    URLConnection con = url.openConnection();
                    //3.2.3)InputStream in = con.getInputStream
                    InputStream in = con.getInputStream();

                    //3.2.4) String json IO.getString(in)
                    String json = IO.getString(in);

                    //3.2.5) parse the json
                    List<Movie> movies = parseJson(json);

                    //3.2.6) notify the listener
                    listener.onMoviesArrived(movies, null);
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onMoviesArrived(null, e);
                }

            }
        });

    }

    //4) parseJson(json)
    private static List<Movie> parseJson(String json) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        JSONArray root = new JSONArray(json);
        for (int i = 0; i < root.length(); i++) {
            JSONObject movieObject = root.getJSONObject(i);
            String title = movieObject.getString("title");
            String image = movieObject.getString("image");
            int releaseYear = movieObject.getInt("releaseYear");
            Double rating = movieObject.getDouble("rating");

            JSONArray genresArray = movieObject.getJSONArray("genre");
            String[] genre = new String[genresArray.length()];
            for (int j = 0; j < genre.length; j++) {
                genre[j] = genresArray.toString(j);
            }

            movies.add(new Movie(title,image,releaseYear,rating,genre));

        }

        return movies;
    }


    //5) notify listener
}
