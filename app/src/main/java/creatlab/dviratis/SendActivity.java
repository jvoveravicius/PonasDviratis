package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.solver.Cache;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class SendActivity extends AppCompatActivity {

    Logs Log = new Logs();
    private static final int CAMERA_REQUEST = 1888; // field
    Context context = this;


    ImageView buckysImageView;
    private SharedPreferences SaveData;
    public static String TicketData = "TicketData";

    Bitmap picture;
    private LruCache<String, Bitmap> mMemoryCache;



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

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };



    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    private boolean hasCamera(){

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    public void goToMapActivity(View view) {

        Intent myIntent = new Intent(SendActivity.this, Map.class);
        SendActivity.this.startActivity(myIntent);

    }

    public void sendData(View view) {

        SendEmail();

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

            // save bitmap to cache directory
            try {

                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time




                picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
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



    void SendEmail(){

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "creatlab.dviratis", newFile);
        String[] TO = {"juozas.voveravicius@gmail.com"};


        if (contentUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }


        /*


        String[] TO = {"juozas.voveravicius@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);


        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

        */

    }








    }





