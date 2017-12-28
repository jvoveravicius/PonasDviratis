package creatlab.dviratis;


import android.util.Log;

public class Logs {


    static private final String INFO_LOG_TAG = "DVIRATIS INFO";
    static private final String ERROR_LOG_TAG = "DVIRATIS ERROR";

    private boolean Logs = true;
    //isjungimas logu appse


    public void Print(int type, String txt){

        if (Logs)
        {
            switch (type) {
                case 0: Log.i(INFO_LOG_TAG, txt);
                    break;
                case 1:  Log.i(ERROR_LOG_TAG, txt);
                    break;

                default: break;

            }

        }

    }

    public void Assert(String txt){

        if (Logs) {

            throw new AssertionError(txt);

        }
        else{
            Log.i(ERROR_LOG_TAG, txt);
        }

    }


}
