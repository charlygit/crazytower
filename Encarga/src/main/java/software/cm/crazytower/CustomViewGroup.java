package software.cm.crazytower;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CustomViewGroup extends LinearLayout {

    public CustomViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("customViewGroup", "**********Intercepted");
        return true;
    }
}