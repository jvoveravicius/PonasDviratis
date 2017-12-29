package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;
import java.util.Locale;

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
                log.Assert("Incorrect parameter in GeoPosition");
            }

        }

        return val;

    }

    public String GeoCoder(){

        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(GeoPosition('a'),GeoPosition('b'), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("loction address", strReturnedAddress.toString());
            } else {
                Log.w("loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("loction address", "Canont get Address!");
        }
        return strAdd;

    }





}