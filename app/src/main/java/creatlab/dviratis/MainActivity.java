package creatlab.dviratis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_PERMISSION = 1;
    public final String  PPERMISSION_DATA = "PermissionData";
    public String[] Permissions = {"GPS0", "EX0", "IN0"};


    Logs Log = new Logs();
    GPSTracking GPS = new GPSTracking(this);
    SaveData Save = new SaveData(this);
    Transitions GoTo = new Transitions();

    CountDownTimer DelayTick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CallPermissions();

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION) {

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.Print(0, permissions+ " [Permission has been granted]");

            } else {

                Log.Print(0, permissions+ " [Permission has been denied]");

            }

            ShowPermissionsInformation();

        }

    }

    private void goToMap() {

        Intent myIntent = new Intent(MainActivity.this, Map.class);
        MainActivity.this.startActivity(myIntent);

    }

    private void goToPermissions(){

        Intent myIntent = new Intent(MainActivity.this, PermissionsActivity.class);
        MainActivity.this.startActivity(myIntent);

    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void CallPermissions(){


        short it = 0;

        if (!GPS.checkLocationPermission()) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        }
        else{

            it++;
        }

        if (!Save.checkExternalStoragePermission()) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }
        else{

            it++;

        }

        if (it == 2){

            Log.Print(0, "All permission was granted!");
            Delay();

        }


    }


    private void ShowPermissionsInformation(){


        boolean Result = false;


        if (!GPS.checkLocationPermission()){
            Permissions[0] = "GPS1";
            Result = true;
        }

        if (!Save.checkExternalStoragePermission()){
            Permissions[1] = "EX1";
            Result = true;
        }

        if (!isNetworkConnected()){

            Log.Print(0, "No internet connection");
            Permissions[2] = "IN1";
            Result = true;

        }


        Save.SaveStringArrayData(PPERMISSION_DATA,  Permissions);



        if (Result){

            GoTo.ActivityTransitions(this, PermissionsActivity.class);

        }
        else{

            GoTo.ActivityTransitions(this, Map.class);

        }


    }

    private void Delay(){


        DelayTick = new CountDownTimer(1000, 800){

            public void onTick(long millisUntilFinished) {

                Log.Print(0, "Seconds " + millisUntilFinished / 1000);

            }

            public void onFinish() {

                if (isNetworkConnected()){

                    Log.Print(0, "Start activity ");
                    goToMap();

                }
                else{

                    Log.Print(0, "No internet connection");
                    Permissions[2] = "IN1";
                    Save.SaveStringArrayData(PPERMISSION_DATA,  Permissions);
                    goToPermissions();

                }

            }

        }.start();

    }


}