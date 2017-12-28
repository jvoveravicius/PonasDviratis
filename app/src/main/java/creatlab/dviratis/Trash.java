package creatlab.dviratis;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class Trash extends AppCompatActivity {

    static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    SharedPreferences someData;

    public static String fileName = "SystemData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

    }

    public void getCurrentLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();


        Log.d("INSTANCE STATE", "Saved: "+ latitude);

    }

    public  void checkPermission(){


        //===========writting data========

        String settingsValue = "boolCheckPermissions";

        someData = getSharedPreferences(fileName, 0);
        boolean dataReturned = someData.getBoolean(settingsValue, true);

        Log.d("INSTANCE STATE", "Returned value: "+String.valueOf(dataReturned));

        //===========writting data========



        if (dataReturned) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            }

            dataReturned = false;

        }


        //===========saving data========
        SharedPreferences.Editor editor = someData.edit();
        editor.putBoolean(settingsValue, dataReturned);
        editor.commit();

        Log.d("INSTANCE STATE", "Saved value: "+String.valueOf(dataReturned));
        //===========saving data========

    }


}
