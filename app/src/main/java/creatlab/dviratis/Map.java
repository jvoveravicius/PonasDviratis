package creatlab.dviratis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Logs Log = new Logs();
    GPSTracking Gps = new GPSTracking(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!Gps.checkLocationPermission()){

            Log.Print(0, "Permission False, access denied");
            Intent myIntent = new Intent(Map.this, ExplanationActivity.class);
            Map.this.startActivity(myIntent);

        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        //----Main Settings
        float MinZoom = 15.00f;
        float MaxZoom = 20.00f;
        String MarkerText = "Jūsų pozicija";
        //----Main Settings


        Log.Print(0, "Initialised latitude = " + Gps.GeoPosition('a'));
        Log.Print(0, "Initialised longitude = " + Gps.GeoPosition('b'));


        LatLng myPosition = new LatLng(Gps.GeoPosition('a'), Gps.GeoPosition('b'));

        mMap = googleMap;
        mMap.setMinZoomPreference(MinZoom);
        mMap.setMaxZoomPreference(MaxZoom);
        mMap.addMarker(new MarkerOptions()

                .position(myPosition).title(MarkerText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)
                ));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));


    }



    public void goToHelp(View view) {

    }


    public void goToTakePicture(View view) {

    }


}
