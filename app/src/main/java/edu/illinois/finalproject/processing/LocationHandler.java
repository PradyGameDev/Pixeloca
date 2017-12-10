package edu.illinois.finalproject.processing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.illinois.finalproject.activities.NewPostActivity;

public class LocationHandler {
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    private TextView locationTextView;
    private Context context;
    private Location lastKnownLocation;
    private GeocodingManager geocodingManager;
    private NewPostActivity activity;
    private boolean haveLocationPermission;

    public LocationHandler(TextView locationTextView, NewPostActivity activity) {
        this.locationTextView = locationTextView;
        this.context = activity;
        this.activity = activity;

        this.setHaveLocationPermission(
                activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED);
        Log.d("ASDF", "" + haveLocationPermission);
    }

    public void setHaveLocationPermission(boolean haveLocationPermission) {
        this.haveLocationPermission = haveLocationPermission;
    }

    @SuppressWarnings({"MissingPermission"})
    public void setUpLocationGathering() {
        if (!haveLocationPermission) {
            return;
        }

        // Acquire a reference to the system Location Manager
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(locationManager);
                Log.v("Location", "New location received.");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                                               locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                                               locationListener);
        if (lastKnownLocation != null) {
            geocodingManager = new GeocodingManager(lastKnownLocation, locationTextView);
            Log.v("LocationLastKnown", geocodingManager.getLastKnownFormattedAddress());
            locationTextView.setText(geocodingManager.getLastKnownFormattedAddress());
        }
    }

    /**
     * Called once a new location is found, for processing the user's current location
     *
     * @param locationManager The manager in the current context.
     */
    @SuppressWarnings({"MissingPermission"})
    private void makeUseOfNewLocation(LocationManager locationManager) {
        Log.v("Location", "Debug");
        if (!haveLocationPermission) {
            return;
        }

        Log.d("Location", "Doing things");
        try {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            geocodingManager = new GeocodingManager(lastKnownLocation, locationTextView);
            Log.v("LocationLastKnown", geocodingManager.getLastKnownFormattedAddress());
            locationTextView.setText(geocodingManager.getLastKnownFormattedAddress());
        } catch (Exception e) {
            Log.d("Location", e.toString());
            activity.askForLocationPermissions();
        }
    }
}
