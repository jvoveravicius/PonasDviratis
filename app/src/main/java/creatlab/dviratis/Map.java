package creatlab.dviratis;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
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
import java.util.HashSet;
import java.util.Set;



public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean UpdateInstantMarker = false;

    public SharedPreferences SaveData;
    ImageView ImageView;
    CountDownTimer UpdateLocationCountDownTimer;

    public static String TicketData = "TicketData";
    public static String  MarkerText = "Jūsų pozicija";
    private static final int CAMERA_REQUEST = 1888;
    float MinZoom = 15.00f;

    Logs Log = new Logs();
    GPSTracking Gps = new GPSTracking(this);
    Transitions GoTo = new Transitions();

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
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
        Log.Print(0, "Initialised latitude = " + Gps.GeoPosition('a'));
        Log.Print(0, "Initialised longitude = " + Gps.GeoPosition('b'));
        mMap = googleMap;
        UpdataMarker();
    }

    private void UpdataMarker(){
        Log.Print(0, "Coordinates updated to "+Gps.GeoPosition('a')+" "+Gps.GeoPosition('b'));
        LatLng myPosition = new LatLng(Gps.GeoPosition('a'), Gps.GeoPosition('b'));
        mMap.clear();
        mMap.setMinZoomPreference(MinZoom);
        mMap.addMarker(new MarkerOptions()
                .position(myPosition).title(MarkerText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)
                ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
    }



    private void RunMarkerTick(){
             UpdateLocationCountDownTimer = new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilFinished) {
                Log.Print(0, "Seconds remaining before next update: " + millisUntilFinished / 1000);

                if (UpdateInstantMarker){
                    UpdataMarker();
                    UpdateInstantMarker = false;
                    Log.Print(0, "Updated marker!");
                }

            }
            public void onFinish() {
                UpdataMarker();
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

        GoTo.ActivityTransitions(this, Map.class);
    }



    public void goToCapture(View view) {
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
        mySet.add("Koordinatės Vilniaus mieste:\n"+Gps.GeoPosition('a')+" š. plat. ir "+Gps.GeoPosition('b')+" r. ilg.\n");
        mySet.add("Artimiausias adresas:\n"+ Gps.GeoCoder());
        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(TicketData,mySet);
        editor.commit();
    }




}
