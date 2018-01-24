package creatlab.dviratis;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SendActivity extends AppCompatActivity {


    Context context = this;
    Bitmap picture;

    public static String TicketData = "TicketData";
    public SharedPreferences SaveData;
    public LruCache<String, Bitmap> mMemoryCache;

    private String ReturnedSaveData;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri fileUri;
    String photoPath = "";

    Logs Log = new Logs();
    Transitions GoTo = new Transitions();


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

            String fileName = System.currentTimeMillis()+".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        }
        else{

            GoTo.ActivityTransitions(this, Map.class);

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

           // File imgFile = new  File(pictureImagePath);

            photoPath = getPath(fileUri);
            File imgFile = new  File(photoPath);

            //File imgFile = new  File(pictureImagePath);
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

    @SuppressWarnings("deprecation")
    private String getPath(Uri selectedImaeUri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(selectedImaeUri, projection, null, null,
                null);

        if (cursor != null)
        {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
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

        String[] TO = {"dviraciai@vilnius.lt","e.vicemeras@vilnius.lt"};

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





