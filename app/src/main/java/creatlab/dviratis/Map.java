package creatlab.dviratis;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.MediaStore;
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

import java.util.HashSet;
import java.util.Set;



public class Map extends FragmentActivity implements OnMapReadyCallback {

    //https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private GoogleMap mMap;
    private SharedPreferences SaveData;

    public static String TicketData = "TicketData";
    private static String  MarkerText = "Jūsų pozicija";


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

        findViewById(R.id.btnUnderstood).setVisibility(View.INVISIBLE);


        if (!Gps.checkLocationPermission()){

            Log.Print(0, "Permission False, access denied");
            Log.Print(0, "Go to Explanation Activity");

            Fragment ExplFrag = new PermissionsExplanationFragment();
            FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
            transaction2.replace(R.id.map, ExplFrag);
            transaction2.addToBackStack(null);
            transaction2.commit();

            Log.Print(0, "Adjusted buttons visibility");
            findViewById(R.id.btnGoToHelp).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnGoTakePict).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnUnderstood).setVisibility(View.VISIBLE);

        }

        UpdateMarker();

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {


        //----Main Settings
        float MinZoom = 15.00f;
        float MaxZoom = 20.00f;
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

    public void UpdateMarker(){


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.Print(0, "Seconds remaining before next update: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.Print(0, "Coordinates updated to "+Gps.GeoPosition('a')+" "+Gps.GeoPosition('b'));

                LatLng myPosition = new LatLng(Gps.GeoPosition('a'), Gps.GeoPosition('b'));
                mMap.clear();
                mMap.addMarker(new MarkerOptions()

                        .position(myPosition).title(MarkerText)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)
                        ));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));

                UpdateMarker();//padariau rekursija
            }
        }.start();


    }



    public void goToHelp(View view) {


        SaveData = getSharedPreferences(TicketData, 0);

        Set<String> numbers = new HashSet<String>();
        numbers = SaveData.getStringSet(TicketData, null);

        if (numbers!=null){
            for (String num : numbers){
                Log.Print(0, num);
            }
        }


    }



    public void goToTakePicture(View view) {

        SaveMapData();
        dispatchTakePictureIntent();

        //Intent myIntent = new Intent(Map.this, SendActivity.class);
        //Map.this.startActivity(myIntent);


    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


        }
    }





    public void goToMainActivity(View view) {

        Intent myIntent = new Intent(Map.this, MainActivity.class);
        Map.this.startActivity(myIntent);

    }

    public void SaveMapData() {

        SaveData = getSharedPreferences(TicketData, 0);
        SaveData.getStringSet(TicketData, null);

        Set<String> mySet = new HashSet<String>();
        mySet.add("Koordinatės: "+Gps.GeoPosition('a')+" šiaurės platumos ir "+Gps.GeoPosition('b')+" rytų ilgumos");
        mySet.add("Adresas: "+ Gps.GeoCoder());


        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(TicketData,mySet);
        editor.commit();


    }




}
