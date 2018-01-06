package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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


    private GoogleMap mMap;
    private SharedPreferences SaveData;

    ImageView buckysImageView;
    CountDownTimer UpdateLocationCountDownTimer;


    private static final int CAMERA_REQUEST = 1888; // field

    public static String TicketData = "TicketData";
    private static String  MarkerText = "Jūsų pozicija";


    Logs Log = new Logs();
    GPSTracking Gps = new GPSTracking(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        buckysImageView = findViewById(R.id.buckysImageView);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //visibilities
        findViewById(R.id.btnUnderstood).setVisibility(View.INVISIBLE);
        findViewById(R.id.BtnGoToMap).setVisibility(View.INVISIBLE);
        //visibilities



        if (!Gps.checkLocationPermission() || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            Log.Print(0, "Permission False, access denied");
            Log.Print(0, "Go to Explanation fragment");

            Fragment ExplFrag = new PermissionsExplanationFragment();
            FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
            transaction2.replace(R.id.map, ExplFrag);
            transaction2.addToBackStack(null);
            transaction2.commit();

            //visibilities
            Log.Print(0, "Adjusted buttons visibility");
            findViewById(R.id.btnGoToHelp).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnGoTakePict).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnUnderstood).setVisibility(View.VISIBLE);
            //visibilities


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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)
                ));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void UpdateMarker(){

        UpdateLocationCountDownTimer = new CountDownTimer(30000, 1000){


            public void onTick(long millisUntilFinished) {
                Log.Print(0, "Seconds remaining before next update: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.Print(0, "Coordinates updated to "+Gps.GeoPosition('a')+" "+Gps.GeoPosition('b'));

                LatLng myPosition = new LatLng(Gps.GeoPosition('a'), Gps.GeoPosition('b'));
                mMap.clear();
                mMap.addMarker(new MarkerOptions()

                        .position(myPosition).title(MarkerText)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)
                        ));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));

                UpdateMarker();//padariau rekursija
            }
        }.start();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            buckysImageView.setImageBitmap(picture); //for example I put bmp in an ImageView
        }
    }



    public void goToHelp(View view) {

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.btnGoToHelp).setAnimation(a);
        view.startAnimation(a);

        findViewById(R.id.btnGoToHelp).setVisibility(View.INVISIBLE);
        findViewById(R.id.BtnGoToMap).setVisibility(View.VISIBLE);

        Log.Print(0, "Go to Help fragment");

        Fragment HFragment = new HelpFragment();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.map, HFragment);
        t.addToBackStack(null);
        t.commit();

    }


    public void goToCapture(View view) {

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.btnGoTakePict).setAnimation(a);
        view.startAnimation(a);

        SaveMapData();
        UpdateLocationCountDownTimer.cancel();

        Intent myIntent = new Intent(Map.this, SendActivity.class);
        Map.this.startActivity(myIntent);


    }



    public void goToMainActivity(View view) {

        Intent myIntent = new Intent(Map.this, MainActivity.class);
        Map.this.startActivity(myIntent);

    }

    public void goToMap(View view) {


        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.BtnGoToMap).setAnimation(a);
        view.startAnimation(a);

        findViewById(R.id.BtnGoToMap).setVisibility(View.VISIBLE);
        Intent myIntent = new Intent(Map.this, Map.class);
        Map.this.startActivity(myIntent);


    }

    public void SaveMapData() {

        SaveData = getSharedPreferences(TicketData, 0);
        SaveData.getStringSet(TicketData, null);

        Set<String> mySet = new HashSet<String>();
        mySet.add("Koordinatės Vilniaus mieste:\n"+Gps.GeoPosition('a')+" š. plat. ir "+Gps.GeoPosition('b')+" r. ilg.\n");
        mySet.add("Artimiausias adresas:\n"+ Gps.GeoCoder());


        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(TicketData,mySet);
        editor.commit();


    }




}
