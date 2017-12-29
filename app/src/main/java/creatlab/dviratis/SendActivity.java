package creatlab.dviratis;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SendActivity extends AppCompatActivity {

    Logs Log = new Logs();
    private static final int CAMERA_REQUEST = 1888; // field

    ImageView buckysImageView;


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
        }

    }

    private boolean hasCamera(){

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    public void goToMapActivity(View view) {

        Intent myIntent = new Intent(SendActivity.this, Map.class);
        SendActivity.this.startActivity(myIntent);

    }


    public void takePicture() {

        Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            buckysImageView.setImageBitmap(picture); //for example I put bmp in an ImageView
        }
    }


}
