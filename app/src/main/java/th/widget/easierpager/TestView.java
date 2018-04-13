package th.widget.easierpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import th.how.app.R;

/**
 * Created by me_touch on 18-3-22.
 *
 */

public class TestView extends TextView {

    private Rect mRect;
    private Paint mPaint;
    private Matrix matrix;
    private Bitmap bitmap;
    private Bitmap newBitmap;
    private float[] elements = new float[9];

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        matrix = new Matrix();
//
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        //matrix.postRotate(180, bitmap.getWidth() /2, bitmap.getHeight());
//        matrix.preScale(2, 2);
//        newBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight() / 2, matrix, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("onTouchEvent", "motionEvent = " + event.getAction());
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int radius = w / 2;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        //canvas.scale(2, 2);
////        canvas.drawBitmap(bitmap, 250, 250, null);
//        //canvas.getMatrix().getValues(elements);
//        //System.out.println(elements[2] + ", " + elements[5]);
//        //canvas.drawBitmap(newBitmap, 100, 100, mPaint);
//
//    }
}
