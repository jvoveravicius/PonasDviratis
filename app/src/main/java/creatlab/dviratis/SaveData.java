package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


public class SaveData {

    Context SaveContext;
    static final int REQUEST_PERMISSION = 1;

    public SaveData(Context mContext) {

        this.SaveContext = SaveContext;

    }


    public boolean checkExternalStoragePermission(){

        boolean permission = true;

        if (ActivityCompat.checkSelfPermission(SaveContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) SaveContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }

        return permission;

    }

}
