package creatlab.dviratis;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by Juozas on 12/18/2017.
 */

public class GPSTracker {

    static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    Context mContext;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
    }
//https://hafizahusairi.com/2017/08/26/tutorial-get-current-latitude-longitude-android-studio-2017/

    public void getLocation() {

        /*

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location!=null){
                double lati = location.getLatitude();
                double longi = location.getLongitude();

            }
            else
            {
                //need something to write here
            }
        */

        }




    }





