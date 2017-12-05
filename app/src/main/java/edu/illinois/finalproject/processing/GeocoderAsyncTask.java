package edu.illinois.finalproject.processing;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import edu.illinois.finalproject.schemas.GoogleMapsApiKey;
import edu.illinois.finalproject.schemas.ReverseGeocoderResponse;

public class GeocoderAsyncTask extends AsyncTask<String, Location, ReverseGeocoderResponse> {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private Location location;
    private GeocodingManager geocodingManager;

    public GeocoderAsyncTask(GeocodingManager geocodingManager, Location location) {
        this.location = location;
        this.geocodingManager = geocodingManager;
    }

    @Override
    protected ReverseGeocoderResponse doInBackground(String... strings) {
        Uri googleMapsGeocoderUrl;
        InputStream inputStream;
        try {
            googleMapsGeocoderUrl = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("latlng", String.format("%s,%s", location.getLatitude()
                            , location.getLongitude()))
                    .appendQueryParameter("API_KEY", GoogleMapsApiKey.API_KEY)
                    .build();
            Log.v("URL", googleMapsGeocoderUrl.toString());
            URLConnection connection = new URL(googleMapsGeocoderUrl.toString()).openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName
                    ("UTF-8"));
            Gson gson = new Gson();
            ReverseGeocoderResponse response = gson.fromJson(inputStreamReader,
                                                             ReverseGeocoderResponse.class);
            Log.v("Gson response", response.getResults()
                    .get(0)
                    .getFormattedAddress());
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReverseGeocoderResponse reverseGeocoderResponse) {
        geocodingManager.onResponseReceived(reverseGeocoderResponse);
    }
}
