package com.example.beavervolunteerappjava;
/**
 *
 * @author  Nina Zhang
 * @version 1.0
 * @since   2022/07/
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public class OpportunityPopUp extends Activity {
    /**
     * This class is for handling the pop up window on volunteering page
     * that shows details of a volunteer opportunity
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opportunity_pop_up);
        //This removes the action bar on top of the activity
        getActionBar().hide();
        //Set new values of the width and height of the new pop up window
        //Basically take the phone resolution and set the window size as that resolution

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // This takes the percentage of the current phone screen and make the pop up 80% of the screen size
        // by multiplying it by 0.8
        getWindow().setLayout((int)(width*0.8), (int)(height*0.8));



    }
}
