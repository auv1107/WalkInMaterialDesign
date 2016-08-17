package com.example.android.materialdesigncodelab.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

import java.io.IOException;

/**
 * Created by lixindong on 8/9/16.
 */
public class MockLocationProvider implements Runnable {
    LocationManager locationManager;
    String mocLocationProvider;
    boolean isRunning = false;
    double longitude = 0;
    double latitude = 0;

    public MockLocationProvider(LocationManager locationManager,
                                String mocLocationProvider) {

        this.locationManager = locationManager;
        this.mocLocationProvider = mocLocationProvider;
    }

    public MockLocationProvider longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
    public MockLocationProvider latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        isRunning = false;
    }

    @SuppressLint("NewApi")
    @Override
    public void run() {

//        Double latitude = 30.673822;
//        Double longitude = 103.988572;
        while (isRunning) {

            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            // Set one position

            Double altitude = 200.0;
            Location location = new Location(mocLocationProvider);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAltitude(altitude);

            // set the time in the location. If the time on this location
            // matches the time on the one in the previous set call, it will
            // be
            // ignored
            location.setTime(System.currentTimeMillis());

            location.setAccuracy((float) 5.0);
            location.setElapsedRealtimeNanos(SystemClock
                    .elapsedRealtimeNanos());
            locationManager.setTestProviderLocation(mocLocationProvider,
                    location);

        }
    }
    static MockLocationProvider sInstance;
    public static MockLocationProvider getInstance(Context context) {
        if (sInstance == null) {
            String provider = LocationManager.GPS_PROVIDER;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.addTestProvider(provider, false, false,
                    false, false, true, true, true, 0, 5);
            locationManager.setTestProviderEnabled(provider, true);
            sInstance = new MockLocationProvider(locationManager, provider);
        }
        return sInstance;
    }

    public static void startMock(Context context, double longitude, double latitude) {
        getInstance(context).latitude(latitude).longitude(longitude).start();
//        getInstance(context).Latigude(latitude).Longitude(longitude).start();
    }

    public static void stopMock(Context context) {
        if (sInstance != null) {
            sInstance.stop();
        }
    }

    public static boolean isRunning(Context context) {
        return getInstance(context).isRunning;
    }

}
