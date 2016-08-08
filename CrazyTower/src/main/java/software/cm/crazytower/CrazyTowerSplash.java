package software.cm.crazytower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class CrazyTowerSplash extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_crazytower_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);
                CrazyTowerSplash.this.startActivity(mainIntent);
                CrazyTowerSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
