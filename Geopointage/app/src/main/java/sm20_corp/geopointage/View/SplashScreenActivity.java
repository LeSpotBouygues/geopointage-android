package sm20_corp.geopointage.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.R;

/**
 * Created by gun on 01/11/2016.
 * Geopointage
 */

public class SplashScreenActivity extends Activity {


    //timer for splash screen in MS
    private final int SPLASH_TIME_OUT = 1000;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        DatabaseHandler.getInstance(this).createChef();

        new Handler().postDelayed(new Runnable() {
            //Showing splash screen with a timer.

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start app main activity
                if (SplashScreenActivity.isActivityVisible()) {
                    // Show login screen
                    if (!DatabaseHandler.getInstance(getApplicationContext()).getChef().getId().equals("-1")) {
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, ChooseActivity.class);
                        i.putExtra("choose", 1);
                        startActivity(i);
                    }
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SplashScreenActivity.activityPaused();
    }

}
