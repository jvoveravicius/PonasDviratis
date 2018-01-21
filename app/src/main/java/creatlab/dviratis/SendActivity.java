package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.constraint.solver.Cache;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Base64;
import android.util.LruCache;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SendActivity extends AppCompatActivity {


    Context context = this;
    Bitmap picture;

    public String pictureImagePath = "";
    public static String TicketData = "TicketData";
    public SharedPreferences SaveData;
    public LruCache<String, Bitmap> mMemoryCache;

    private static final int CAMERA_REQUEST = 1888;
    public final String  CAMERA_DATA = "Camera";
    private String ReturnedSaveData;

    Logs Log = new Logs();
    Transitions GoTo = new Transitions();
    SaveData Save = new SaveData(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        takePictureFromPhoto();
        getSaveData();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };


    }


    public String getRadioButtonText() {

        RadioGroup radioGroup;
        RadioButton radioButton;

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        String selection = (String) radioButton.getText();
        Log.Print(0,selection);

        return selection;

    }


    public void goToMapActivity(View view) {
        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.closeSendActivity).setAnimation(a);
        GoTo.ActivityTransitions(this, Map.class);
    }



    public void sendData(View view) {

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.sendEmail).setAnimation(a);
        view.startAnimation(a);
        SendEmail();

    }



    public void takePictureFromPhoto() {

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {

            Log.Print(0, "Camera activated");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, 1);
        }
        else{

            GoTo.ActivityTransitions(this, Map.class);

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                picture = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                try {

                    File cachePath = new File(context.getCacheDir(), "images");
                    cachePath.mkdirs(); // don't forget to make the directory
                    FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageView myImage = (ImageView) findViewById(R.id.SendImageView);
                myImage.setImageBitmap(picture);

            }
            else{

                GoTo.ActivityTransitions(this, Map.class);
                Log.Print(0, "Image not received!");
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
                MainString = num +"\n"+ MainString;

            }
        }

        ToView.setText(MainString);
        ReturnedSaveData = MainString;

    }


    void SendEmail(){

        String RadioButtonValue = getRadioButtonText();

        String Greetings = "Sveiki, \n\n Vilniaus mieste užfiksuota kliūtis dviratininkams, kuri trukdo saugiam dviratininkų eismui. \nNuotrauka su kliūtimi prikabinta prie laiško.\n\n";
        String Priority = "Kliūties kritiškumas yra "+RadioButtonValue+".\n\n";

        String[] TO = {"juozas.voveravicius@gmail.com"};

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "creatlab.dviratis", newFile);


        if (contentUri != null) {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            //emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SU DVIRAČIU BE KLIŪČIŲ!");
            emailIntent.putExtra(Intent.EXTRA_TEXT, Greetings+Priority+ReturnedSaveData);
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SendActivity.this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }

        }


    }

}





