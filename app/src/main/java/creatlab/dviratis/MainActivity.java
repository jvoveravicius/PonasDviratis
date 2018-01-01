package creatlab.dviratis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_LOCATION = 1;

    CountDownTimer StartTick;

    Logs Log = new Logs();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            Log.Print(0, "Location permission was granted!");
            Delay();

        }

    }


    //callback from permission
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.Print(0, "Permission has been granted");

            } else {

                Log.Print(0, "Permission has been denied");

            }

            goToMap();

        }

    }

    public void goToMap() {

        Intent myIntent = new Intent(MainActivity.this, Map.class);
        MainActivity.this.startActivity(myIntent);

    }


    private void Delay(){

        StartTick = new CountDownTimer(1000, 800){

            public void onTick(long millisUntilFinished) {

                Log.Print(0, "Seconds " + millisUntilFinished / 1000);

            }

            public void onFinish() {

                Log.Print(0, "Start activity ");
                Intent myIntent = new Intent(MainActivity.this, Map.class);
                MainActivity.this.startActivity(myIntent);

            }

        }.start();

    }




}