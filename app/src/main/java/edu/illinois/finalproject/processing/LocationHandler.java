package edu.illinois.finalproject.processing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

public class LocationHandler {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 0;
    private TextView locationTextView;
    private Context context;
    private Location lastKnownLocation;
    private GeocodingManager geocodingManager;

    public LocationHandler(TextView locationTextView, Context context) {
        this.locationTextView = locationTextView;
        this.context = context;
    }

    public void setUpLocationGathering() {
        if (ContextCompat.checkSelfPermission(context,
                                              Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                                                    Manifest.permission
                                                                            .ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) context,
                                                  new String[]{Manifest
                                                          .permission_group.LOCATION},
                                                  MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                                                  new String[]{Manifest
                                                          .permission_group.LOCATION},
                                                  MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                // MY_PERMISSIONS_REQUEST_ACCESS_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                                                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                                              new String[]{Manifest
                                                      .permission_group.LOCATION},
                                              MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                                               locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                                               locationListener);
        geocodingManager = new GeocodingManager(
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER),
                locationTextView);
        locationTextView.setText(geocodingManager.getLastKnownFormattedAddress());
    }

    /**
     * Called once a new location is found, for processing the user's current location
     *
     * @param locationManager The manager in the current context.
     */
    private void makeUseOfNewLocation(LocationManager locationManager) {
        Log.v("Location", "Debug");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                                                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                                              new String[]{Manifest
                                                      .permission_group.LOCATION},
                                              MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
        locationTextView.setText(
                (CharSequence) locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                                                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                                              new String[]{Manifest
                                                      .permission_group.LOCATION},
                                              MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        geocodingManager = new GeocodingManager(lastKnownLocation, locationTextView);
        Log.v("LocationLastKnown", geocodingManager.getLastKnownFormattedAddress());
        locationTextView.setText(geocodingManager.getLastKnownFormattedAddress());
    }
}
