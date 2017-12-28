package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class GPSTracking {

    Logs log = new Logs();

    Context mContext;
    LocationManager locationManager;


    public GPSTracking(Context mContext) {
        //need to make construct to pass all content. Didn't find better solution
        this.mContext = mContext;

    }


    public boolean checkLocationPermission(){

        boolean permission = true;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission = false;
        }

        return permission;

    }

    public double GeoPosition(char coord){

        double val = 0;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (checkLocationPermission()){

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (coord=='a'){
                val = location.getLatitude();
                log.Print(0, "getLatitude "+val);
            }
            else if (coord=='b'){

                val = location.getLongitude();
                log.Print(0, "getLatitude "+val);

            }
            else{

                throw new AssertionError("Incorrect parameter in GeoPosition");

            }

        }

        return val;

    }


    public double Latitude(){

        double val = 0;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (checkLocationPermission()){

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            val = location.getLatitude();
            log.Print(0, "getLatitude "+val);
        }

        return val;

    }


    public double Longitude(){

        double val = 0;

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission()){

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            val = location.getLongitude();
            log.Print(0, "getLongitude "+val);
        }

        return val;

    }





}