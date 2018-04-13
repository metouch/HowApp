package th.widget.easierpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by me_touch on 18-3-27.
 *
 */

public class CanvasVonvertTouchTest extends View {
    float down_x = -1;
    float down_y = -1;
    int mViewWidth, mViewHeight;

    private Paint mDeafultPaint;

    public CanvasVonvertTouchTest(Context context) {
        this(context, null);
    }

    public CanvasVonvertTouchTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mDeafultPaint = new Paint();
        mDeafultPaint.setAntiAlias(true);
        mDeafultPaint.setStyle(Paint.Style.FILL);
        mDeafultPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // ▼ 注意此处使用 getRawX，而不是 getX
                down_x = event.getX();
                down_y = event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                down_x = down_y = -1;
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] pts = {down_x, down_y};

        drawTouchCoordinateSpace(canvas);            // 绘制触摸坐标系，灰色
        // ▼注意画布平移
        canvas.translate(mViewWidth/2, mViewHeight/2);

        drawTranslateCoordinateSpace(canvas);        // 绘制平移后的坐标系，红色

        if (pts[0] == -1 && pts[1] == -1) return;    // 如果没有就返回

        // ▼ 获得当前矩阵的逆矩阵
        Matrix invertMatrix = new Matrix();
        //canvas.getMatrix().invert(invertMatrix);

        // ▼ 使用 mapPoints 将触摸位置转换为画布坐标
        canvas.getMatrix().mapPoints(pts);

        // 在触摸位置绘制一个小圆
        canvas.drawCircle(pts[0],pts[1],20,mDeafultPaint);
    }

    /**
     *  绘制触摸坐标系，颜色为灰色，为了能够显示出坐标系，将坐标系位置稍微偏移了一点
     */
    private void drawTouchCoordinateSpace(Canvas canvas) {
        canvas.save();
        canvas.translate(10,10);
//        CanvasAidUtils.set2DAxisLength(1000, 0, 1400, 0);
//        CanvasAidUtils.setLineColor(Color.GRAY);
//        CanvasAidUtils.draw2DCoordinateSpace(canvas);
        canvas.restore();
    }

    /**
     * 绘制平移后的坐标系，颜色为红色
     */
    private void drawTranslateCoordinateSpace(Canvas canvas) {
//        CanvasAidUtils.set2DAxisLength(500, 500, 700, 700);
//        CanvasAidUtils.setLineColor(Color.RED);
//        CanvasAidUtils.draw2DCoordinateSpace(canvas);
//        CanvasAidUtils.draw2DCoordinateSpace(canvas);
    }
}
