package com.itayc14.medicalfinder;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.text.DecimalFormat;

/**
 * Created by itaycohen on 21.11.2016.
 */

public class MyLocationListener implements LocationListener {

    private Context context;
    private DistanceViewUpdater dvu;
    public static boolean hasLocation;
    public static Location myLocation;

    public MyLocationListener(Context context, DistanceViewUpdater dvu) {
        this.context = context;
        this.dvu = dvu;
        hasLocation = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!hasLocation) {
            hasLocation = true;
            dvu.enableDistanceLimitationSetting(true);
        }
        myLocation = location;
        dvu.updateDistance();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        hasLocation = false;
    }

    public String distanceToAsString(double lat, double lng){
        double distance = distanceTo(lat, lng);
        DecimalFormat df = new DecimalFormat("#.##");
        String distanceS = df.format(distance)+" meters";
        if(distance > 1000)
            distanceS = df.format(distance/1000)+"km";
        return distanceS;
    }

    public double distanceTo(double lat, double lng){
        Location dest = new Location("");
        dest.setLatitude(lat);
        dest.setLongitude(lng);
        return myLocation.distanceTo(dest);
    }
}
