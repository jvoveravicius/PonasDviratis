package creatlab.dviratis;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


    public class ExternalStoragePermissionFragment extends Fragment {

        Logs log = new Logs();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_external_storage_permission, container, false);
        }


        @Override
        public void onStart() {
            log.Print(0, "ExternalStoragePermissionFragment started");
            super.onStart();
        }

        @Override
        public void onStop() {
            log.Print(0, "ExternalStoragePermissionFragment stopped");
            super.onStop();
        }



    }
