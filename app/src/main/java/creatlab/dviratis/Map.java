package creatlab.dviratis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean UpdateInstantMarker = false;
    private boolean UpdateWarningMarker = true;

    public SharedPreferences SaveData;
    ImageView ImageView;
    CountDownTimer UpdateLocationCountDownTimer;

    public static String TicketData = "TicketData";
    public static String MarkerText = "Jūsų pozicija";
    public static String WarningMarkerText = "Pažymėta vieta";
    public static String WARNING_SAVE = "WarningSigns";
    private static final int CAMERA_REQUEST = 1888;
    float MinZoom = 15.00f;

    Logs Log = new Logs();
    GPSTracking Gps = new GPSTracking(this);
    Transitions GoTo = new Transitions();
    SaveData Save = new SaveData(this);

    double GPSLatitude = 0;
    double GPSLongitude = 0;

    LocationManager locationManager;
    Context mContext;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ImageView = findViewById(R.id.SendImageView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.btnUnderstood).setVisibility(View.INVISIBLE);
        findViewById(R.id.BtnGoToMap).setVisibility(View.INVISIBLE);


        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListenerGPS);

    }


    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            GPSLatitude=location.getLatitude();
            GPSLongitude=location.getLongitude();
            String msg="New Latitude: "+GPSLatitude + "New Longitude: "+GPSLongitude;
            Log.Print(0, msg);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };







    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            LoadDataWarning();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPause(){
        super.onPause();
        UpdateInstantMarker = true;
        UpdateLocationCountDownTimer.cancel();
    }

    @Override
    public void onResume(){

        super.onResume();
        RunMarkerTick();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.Print(0, "Initialised latitude = " + ReceiveCoordinates('a'));
        Log.Print(0, "Initialised longitude = " + ReceiveCoordinates('b'));
        mMap = googleMap;
        UpdataMarker();
    }

    private double ReceiveCoordinates(char v){

        double Lat = 0.0;
        double Long = 0.0;
        double Return = 0.0;

        if (GPSLatitude!=0.0){

            Lat = GPSLatitude;
            Long = GPSLongitude;

        }

        else{

            Lat = Gps.GeoPosition('a');
            Long = Gps.GeoPosition('b');

        }



        if (v=='a'){
            Return = Lat;
        }
        else if (v=='b'){
            Return = Long;
        }


        return Return;

    }



    private void UpdataMarker(){


        Log.Print(0, "UpdataMarker Coordinates updated to "+ReceiveCoordinates('a')+" "+ReceiveCoordinates('b'));
        LatLng myPosition = new LatLng(ReceiveCoordinates('a'), ReceiveCoordinates('b'));
        mMap.clear();
        mMap.setMinZoomPreference(MinZoom);
        mMap.addMarker(new MarkerOptions()
                .position(myPosition).title(MarkerText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)
                ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));


    }

    private void LoadDataWarning(){

        if (Save.LoadStringArrayValue(WARNING_SAVE)!=0){

            String[] Initial = new String[1];
            Initial = Save.LoadStringArrayData(WARNING_SAVE, 1);
            LoadWarningMarker(Initial[0]);

        }
    }

    private String SaveDataWarning(){

        String AddData = ReceiveCoordinates('a')+"*"+ReceiveCoordinates('b')+'#';

        int l = Save.LoadStringArrayValue(WARNING_SAVE);

        String[] Initial = new String[1];

        if (l!=0){
            Initial = Save.LoadStringArrayData(WARNING_SAVE, Initial.length);

                String Value = Initial[0]+AddData;
                Initial[0] = Value;

        }
        else{

            Initial[0] = AddData;

        }


        Save.SaveStringArrayData(WARNING_SAVE, Initial);


        return Initial[0];

    }

    private void LoadWarningMarker(String InitialData){

        String Longitude = "";
        String Latitude = "";


        char[] c_arr = InitialData.toCharArray();
        boolean Long = false;


        for (int i = 0;i<c_arr.length;i++){

            if (!Long){

                Log.Print(0, "Patekau!!!");

                if (c_arr[i]=='*'){
                    Log.Print(0, "Result LOG"+Longitude);

                    Long = true;
                    continue;
                }

                Longitude = Longitude + c_arr[i];


            }
            else{

                if (c_arr[i]=='#'){
                    Log.Print(0, "Result LAT"+Latitude);

                    double Lo = Double.parseDouble(Longitude);
                    double La = Double.parseDouble(Latitude);
                    LoadWarningMarker(Lo, La);

                    Longitude = "";
                    Latitude = "";
                    Long = false;
                    continue;
                }

                Latitude=Latitude+c_arr[i];

            }

            //Log.Print(0, "COMPLETED!!");

        }



    }



    private void LoadWarningMarker(Double Latitude, Double Longitude){

        Log.Print(0, "AddProblemMarker Coordinates updated to "+ReceiveCoordinates('a')+" "+ReceiveCoordinates('b'));
        LatLng myPosition = new LatLng(Latitude, Longitude);

        mMap.addMarker(new MarkerOptions()
                .position(myPosition).title(WarningMarkerText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_marker)
                ));
    }



        private void RunMarkerTick(){
             UpdateLocationCountDownTimer = new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilFinished) {
                Log.Print(0, "Seconds remaining before next update: " + millisUntilFinished / 1000);


                if (millisUntilFinished / 1000 == 28){

                    UpdataMarker();
                    Log.Print(0, "Painted main marker!");

                    Log.Print(0, "Updated Warning Marker!");

                    if (Save.LoadStringArrayValue(WARNING_SAVE)!=0){

                        LoadDataWarning();

                    }

                }




            }
            public void onFinish() {
                UpdataMarker();

                if (Save.LoadStringArrayValue(WARNING_SAVE)!=0){

                    Log.Print(0, "Painted Warning Marker!");

                    LoadDataWarning();

                }

                RunMarkerTick();


            }
        }.start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            ImageView.setImageBitmap(picture);
        }
    }



    public void goToHelp(View view) {

        Log.Print(0, "Go to Help fragment");

        UpdateLocationCountDownTimer.cancel();
        findViewById(R.id.btnGoToHelp).setVisibility(View.INVISIBLE);
        findViewById(R.id.BtnGoToMap).setVisibility(View.VISIBLE);
        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.btnGoToHelp).setAnimation(a);
        view.startAnimation(a);

        Fragment HFragment = new HelpFragment();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.map, HFragment);
        t.addToBackStack(null);
        t.commit();

    }



    public void goToMap(View view) {
        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.btnGoTakePict).setAnimation(a);
        view.startAnimation(a);

        GoTo.ActivityTransitions(this, Map.class);
    }



    public void goToCapture(View view) {
            /*

        double test = ReceiveCoordinates('a');
        double test1 = ReceiveCoordinates('b');
        String total2 = Double.toString(test);
        String total22 = Double.toString(test1);
        Log.Print(0,total2);
        Log.Print(0,total22);

        */


        SaveDataWarning();

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.btnGoTakePict).setAnimation(a);
        view.startAnimation(a);

        UpdateLocationCountDownTimer.cancel();
        SaveMapData();
        GoTo.ActivityTransitions(this, SendActivity.class);



    }



    public void SaveMapData() {
        SaveData = getSharedPreferences(TicketData, 0);
        SaveData.getStringSet(TicketData, null);
        Set<String> mySet = new HashSet<String>();
        mySet.add("Koordinatės Vilniaus mieste:\n"+ReceiveCoordinates('a')+" š. plat. ir "+ReceiveCoordinates('b')+" r. ilg.\n");
        mySet.add("Artimiausias adresas:\n"+ Gps.GeoCoder());
        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(TicketData,mySet);
        editor.commit();
    }




}
