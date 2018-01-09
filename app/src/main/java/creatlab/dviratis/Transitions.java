package creatlab.dviratis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class Transitions
{

    Logs Log = new Logs();




    public void ActivityTransitions(Context Current, Class New) {


        Intent myIntent = new Intent(Current, New);
        Current.startActivity(myIntent);
        Log.Print(0, "Activity go to "+New);

    }

    public void FragmentTranstitions() {


    }

    public void EventsAnimation(){


    }


}
