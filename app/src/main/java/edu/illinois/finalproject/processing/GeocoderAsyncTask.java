package edu.illinois.finalproject.processing;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import edu.illinois.finalproject.schemas.APIKeys;
import edu.illinois.finalproject.schemas.ReverseGeocoderResponse;

/**
 * Manages the API call to the Google Maps Geocoding API.
 * On successful completion, it sets the given TextView to the formatted address
 */
public class GeocoderAsyncTask extends AsyncTask<String, Location, ReverseGeocoderResponse> {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private Location location;
    private GeocodingManager geocodingManager;
    private TextView locationTextView;

    public GeocoderAsyncTask(GeocodingManager geocodingManager, Location location,
                             TextView locationTextView) {
        this.location = location;
        this.geocodingManager = geocodingManager;
        this.locationTextView = locationTextView;
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
                    .appendQueryParameter("API_KEY", APIKeys.API_KEY)
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
            return response;
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
