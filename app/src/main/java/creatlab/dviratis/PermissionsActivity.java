package creatlab.dviratis;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class PermissionsActivity extends AppCompatActivity {

    SaveData Save = new SaveData(this);
    Logs Log = new Logs();
    MainActivity Main = new MainActivity();
    Transitions GoTo = new Transitions();

    Fragment GPSFragment = new GPSPermissionFragment();
    Fragment NoInternetFragment = new NoInternetFragment();
    Fragment ExterStorageFragment = new ExternalStoragePermissionFragment();


    private boolean GPS = false;
    private boolean Ext = false;
    private boolean Int = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        GetAllPermissions();
        ApplyAllPermissions();


    }


    public void goToTest(View view) {

        final Animation a = AnimationUtils.loadAnimation(this, R.anim.onclick);
        findViewById(R.id.bntPermissionOkay).setAnimation(a);
        view.startAnimation(a);
        ApplyAllPermissions();

    }


    public void ApplyAllPermissions(){

        if (GPS){

            Log.Print(0, "GPS Permission was activated");
            GPS = false;

            Log.Print(0, "Go to GPSPermissionFragment");
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.PermissionFragment, GPSFragment);
            t.addToBackStack(null);
            t.commit();


        }
        else if (Ext){

            Log.Print(0, "External storage permission was activated");
            Ext = false;

            Log.Print(0, "Go to ExternalStoragePermissionFragment");
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.PermissionFragment, ExterStorageFragment);
            t.addToBackStack(null);
            t.commit();

        }
        else if (Int){

            Log.Print(0, "No internet is permission is activated");
            Int = false;

            Log.Print(0, "Go to NoInternetFragment");
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.PermissionFragment, NoInternetFragment);
            t.addToBackStack(null);
            t.commit();


        }
        else{

            Log.Print(0, "No more permissions");
            GoTo.ActivityTransitions(this, MainActivity.class);


        }


    }

    private void GetAllPermissions(){


        String AllPermissions[] = Save.LoadStringArrayData(Main.PPERMISSION_DATA, Main.Permissions.length);


        int l = Main.Permissions.length;


        for (int i =0; i<l;i++){

            boolean Activate = true;

            for (int z = 0; z< l; z++){

                if (Main.Permissions[i].equals(AllPermissions[z])){

                    Log.Print(0, "Fine with: "+Main.Permissions[i]);
                    Activate = false;
                    continue;

                }

            }

            if (Activate){

                switch (i) {
                    case 0:
                        GPS = true;
                        break;
                    case 1:
                        Ext = true;
                        break;
                    case 2:
                        Int = true;
                        break;
                    default:
                        Log.Assert("Invalid Main.Permissions.length");
                        break;
                }


            }


        }


    }



}


