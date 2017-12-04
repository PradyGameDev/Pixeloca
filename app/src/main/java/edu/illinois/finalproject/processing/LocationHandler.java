package edu.illinois.finalproject.processing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class LocationHandler {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 0;
    private TextView locationTextView;
    private Context context;
    private Location lastKnownLocation;

    public LocationHandler(TextView locationTextView, Context context) {
        this.locationTextView = locationTextView;
        this.context = context;
    }

    public String getLastKnownLocation() {
        return fetchAddress(lastKnownLocation);
    }

    /**
     * Uses a geocoder to get an address from coordinates.
     *
     * @param location The location the photogra[h was taken at.
     * @return An real-world address of the given location.
     */
    private String fetchAddress(Location location) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                                                 5);
            return addresses.get(0)
                    .toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location.toString();
    }

    public void setupLocationGathering() {
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

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
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
        locationTextView.setText(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                         .toString());

    }

    /**
     * Called once a new location is found, for processing the user's current lcoation
     *
     * @param locationManager The manager in the current context.
     */
    private void makeUseOfNewLocation(LocationManager locationManager) {
        Toast.makeText(context, locationManager.toString(), Toast.LENGTH_SHORT)
                .show();
        Log.v("Location", locationManager.toString());
        Log.v("Location", "Debug");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                                                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationTextView.setText(
                (CharSequence) locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                                                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationTextView.setText(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                         .toString());
        lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}
