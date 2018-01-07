package creatlab.dviratis;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PermissionsActivity extends AppCompatActivity {

    SaveData Save = new SaveData(this);
    Logs Log = new Logs();
    MainActivity Main = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ApplyPermissionFragment();

    }

    private void ApplyPermissionFragment(){

        String AllPermissions[] = Save.LoadStringArrayData(Main.PPERMISSION_DATA, Main.Permissions.length);

        Log.Print(0, "Result " + AllPermissions[0]);
        Log.Print(0, "Result " + AllPermissions[1]);

        if (!AllPermissions[0].equals(Main.Permissions[1])){

            Log.Print(0, AllPermissions[0]+ " not equal " + Main.Permissions[1]);

            Fragment fragment= new ExternalStoragePermissionFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.PermissionFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        if (!AllPermissions[1].equals(Main.Permissions[0])){

            Log.Print(0, AllPermissions[1]+"Not equal " + Main.Permissions[0]);

            Fragment fragment= new GPSPermissionFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.PermissionFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }


    }

}
