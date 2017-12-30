package creatlab.dviratis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class SendActivity extends AppCompatActivity {

    Logs Log = new Logs();
    private static final int CAMERA_REQUEST = 1888; // field

    ImageView buckysImageView;
    private SharedPreferences SaveData;
    public static String TicketData = "TicketData";
    Bitmap picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);


        buckysImageView = findViewById(R.id.buckysImageView);



        if (!hasCamera()){
            findViewById(R.id.btnGoTakePict).setEnabled(false);
            Log.Assert("Need to add fragment");
        }
        else{

            Log.Print(0, "Camera activated");
            takePicture();
            getSaveData();

        }

    }

    private boolean hasCamera(){

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    public void goToMapActivity(View view) {

        Intent myIntent = new Intent(SendActivity.this, Map.class);
        SendActivity.this.startActivity(myIntent);

    }

    public void sendData(View view) {

        sendEmail();

    }


    public void takePicture() {

        Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            buckysImageView.setImageBitmap(picture); //for example I put bmp in an ImageView;
            //https://stackoverflow.com/questions/20656649/how-to-convert-bitmap-to-png-and-then-to-base64-in-android
        }
    }

    public void getSaveData() {

        String MainString = "";
        SaveData = getSharedPreferences(TicketData, 0);
        TextView ToView = (TextView)findViewById(R.id.MainTextView);

        Set<String> numbers = new HashSet<String>();
        numbers = SaveData.getStringSet(TicketData, null);

        if (numbers!=null){
            for (String num : numbers){
                Log.Print(0, num);
                MainString = MainString +"\n"+ num;

            }
        }

        ToView.setText(MainString);


    }



    protected void sendEmail() {

        String[] TO = {"someone@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        emailIntent.putExtra(Intent.EXTRA_STREAM, picture);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
