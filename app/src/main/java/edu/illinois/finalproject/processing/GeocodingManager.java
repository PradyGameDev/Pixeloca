package edu.illinois.finalproject.processing;

import android.location.Location;
import android.util.Log;

import edu.illinois.finalproject.schemas.ReverseGeocoderResponse;

public class GeocodingManager {
    private String lastKnownFormattedAddress;
    private Location location;

    public GeocodingManager(Location location) {
        this.location = location;
        this.lastKnownFormattedAddress = String.format("%s, %s", location.getLatitude(), location
                .getLongitude());
        fetchAddress(location);
    }

    private static void fetchAddress(Location location) {
        GeocoderAsyncTask asyncTask = new GeocoderAsyncTask(location);
        asyncTask.execute();
    }

    public String getLastKnownFormattedAddress() {
        return lastKnownFormattedAddress;
    }

    /**
     * For internal use only. To get an address of a location, call fetchAddress.
     * Called once the AsyncTask returns a response.
     *
     * @param location The location to be reverse-geocoded.
     * @param response The response from the GeocoderAsyncTask.
     * @return A formatted real-world address of the given coordinates. If the AsyncTask fails,
     * it returns a formatted string with coordinates.
     */
    public void onResponseReceived(Location location, ReverseGeocoderResponse response) {

        if (response == null) {
            lastKnownFormattedAddress = String.format("%s, %s", location.getLatitude(), location
                    .getLongitude());
        }
        String formattedAddress = response.getResults()
                .get(0)
                .getFormattedAddress();
        lastKnownFormattedAddress = formattedAddress;
        Log.v("Location", formattedAddress);
    }
}
