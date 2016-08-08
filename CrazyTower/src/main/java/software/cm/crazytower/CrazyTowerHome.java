package software.cm.crazytower;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class CrazyTowerHome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crazytower_homescreen);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("Touch", "Se tocó la pantalla");

        return super.onTouchEvent(event);
    }
}
