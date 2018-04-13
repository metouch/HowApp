package th.widget.easierpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by me_touch on 18-3-21.
 *
 */

public class TestScrollView extends ScrollView {
    public static final String TAG = "TestScrollView";
    public TestScrollView(Context context) {
        super(context);
    }

    public TestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent = " + ev.getActionMasked());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent = " + ev.getActionMasked());
        return super.onTouchEvent(ev);
    }
}
