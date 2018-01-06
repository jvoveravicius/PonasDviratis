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

        for (int i=0; i< AllArray.length;i++){

            mySet.add(AllArray[i]);
            Log.Print(0, "SaveStringArrayData(): FileName "+SaveDataName+" "+i+" "+" "+AllArray[i]);

        }

        SharedPreferences.Editor editor = SaveData.edit();
        editor.putStringSet(SaveDataName,mySet);
        editor.commit();


    }


    public void LoadStringArrayData(String SaveDataName, int Lenght){

        String[] AllToString = new String[Lenght];

        int it = 0;

        SaveData = SaveContext.getSharedPreferences(SaveDataName, 0);
        Set<String> numbers = new HashSet<String>();
        numbers = SaveData.getStringSet(SaveDataName, null);

        if (numbers!=null){

            for (String num : numbers){

                AllToString[it] = num;
                Log.Print(0, Integer.toString(it));
                Log.Print(0, "LoadStringArrayData() FileName"+SaveDataName+" "+AllToString[it]);
                it++;

            }
        }

    }

}
