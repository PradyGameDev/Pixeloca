package edu.illinois.finalproject.processing;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import edu.illinois.finalproject.schemas.ReverseGeocoderResponse;

public class GeocodingManager {
    private String lastKnownFormattedAddress;
    private Location location;
    private TextView textView;

    public GeocodingManager(Location location, TextView textView) {
        this.location = location;
        this.lastKnownFormattedAddress = String.format("%s, %s", location.getLatitude(), location
                .getLongitude());
        this.textView = textView;
        fetchAddress(location);
    }

    private void fetchAddress(Location location) {
        GeocoderAsyncTask asyncTask = new GeocoderAsyncTask(this, location, textView);
        asyncTask.execute();
    }

    public String getLastKnownFormattedAddress() {
        return lastKnownFormattedAddress;
    }

    /**
     * For internal use only. To get an address of a location, call fetchAddress.
     * Called once the AsyncTask returns a response.
     *
     * @param response The response from the GeocoderAsyncTask.
     * @return A formatted real-world address of the given coordinates. If the AsyncTask fails,
     * it returns a formatted string with coordinates.
     */
    public void onResponseReceived(ReverseGeocoderResponse response) {
        boolean geocoderFoundAddress =
                response != null && response.getResults() != null && !response.getResults()
                        .isEmpty();
        if (geocoderFoundAddress) {
            String formattedAddress = response.getResults()
                    .get(0)
                    .getFormattedAddress();
            lastKnownFormattedAddress = formattedAddress;
            Log.v("Location", lastKnownFormattedAddress);
        } else {
            lastKnownFormattedAddress = String.format("%s, %s", location.getLatitude(), location
                    .getLongitude());
        }
        textView.setText(lastKnownFormattedAddress);
    }
}
