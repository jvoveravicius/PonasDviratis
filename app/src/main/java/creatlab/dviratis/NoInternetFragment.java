package creatlab.dviratis;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NoInternetFragment extends Fragment {

    Logs log = new Logs();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_internet, container, false);

    }


    @Override
    public void onStart() {

        log.Print(0, "Start fragment NoInternetFragment");
        super.onStart();


    }

    @Override
    public void onStop() {
        log.Print(0, "Stopped fragment NoInternetFragment");
        super.onStop();
    }

}
