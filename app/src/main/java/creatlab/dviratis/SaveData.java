package creatlab.dviratis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;


public class SaveData {

    Logs Log = new Logs();

    Context SaveContext;

    private SharedPreferences SaveData;

    static final int REQUEST_PERMISSION = 1;

    public SaveData(Context SaveContext) {

        this.SaveContext = SaveContext;

    }


    public boolean checkExternalStoragePermission(){

        boolean permission = true;


        if (ActivityCompat.checkSelfPermission(SaveContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permission = false;
        }

        return permission;

    }



    public void SaveStringArrayData(String SaveDataName, String[] AllArray) {


        SaveData = SaveContext.getSharedPreferences(SaveDataName, 0);
        SaveData.getStringSet(SaveDataName, null);

        Set<String> mySet = new HashSet<String>();

        for (int i = 0;i<AllArray.length;i++){

            mySet.add(AllArray[i]);

        }


        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(SaveDataName,mySet);
        editor.commit();


    }


    public String[] LoadStringArrayData(String SaveDataName, int Arr){

        String[] Result = new String[Arr];
        int it = 0;

        SaveData = SaveContext.getSharedPreferences(SaveDataName, 0);

        Set<String> numbers = new HashSet<String>();
        numbers = SaveData.getStringSet(SaveDataName, null);

        if (numbers!=null){
            for (String num : numbers){

                Log.Print(0, "Loaded save file: "+num);
                Result[it] = num;
                it++;
            }
        }

        return Result;

    }


}
