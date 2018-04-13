package th.how.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import th.how.app.R;
import th.how.utils.Utils;

/**
 * Created by me_touch on 17-11-13.
 *半成品,将就能用, 应该用static修饰变量itemMargin的,
 * 以达到后续每个labelView的itemMargin的值都一样
 */

public class LabelView extends View {

    private static final String TAG = "LabelView";
    private static final int VIRTUAL_COUNT = 4;

    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TEXT_COLOR = 0xffff6600;
    private static final int DEFAULT_BG_COLOR = 0xffff0000;



    private TextPaint textPaint;
    private Paint mPaint;
    private TextPaint.FontMetrics mMetrics;
    private ArrayList<Item> items = new ArrayList<>();
    private Rect mRect = new Rect();
    private RectF mArea = new RectF();
    private Item currentItem; //被click的item

    private int gravity;
    private int textSize;
    private int textColor;
    private int minItemWidth;
    private int itemsWidth;
    private int itemHeight;
    private int itemMargin;
    private int itemCount;
    private int hSpacePadding; //每个item的文字与边缘水平间距,文字居中, 即左space = 右space
    private int vSpacePadding; //文字与边缘的垂直间距, 文字居中.
    private boolean outside;

    float tx = 0; float ty = 0;



    private String[] label = new String[]{"政务", "视频", "思享"};
    private ArrayList<Item> selectedLabels;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr, 0);
        initPaint();
        setLabel(label);
        itemCount = label.length;

    }

    @TargetApi(value = 21)
    public LabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
        setLabel(label);
        itemCount = label.length;
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //String text = "政务视频思享财经";
        //textPaint.getTextBounds(text, 0, text.length(), mRect);
        //canvas.drawColor(0xffff0000);
        //canvas.drawText(text, (mRect.left + mRect.right) / 2, -mMetrics.top, textPaint);
        int itemHeight = getMeasuredHeight();
        int start = 0;
        if(gravity == 2){
            start = (getMeasuredWidth() - itemsWidth - (items.size() - 1)* itemMargin) / 2;
        }
        if(gravity == 3){
            start = getMeasuredWidth() - itemsWidth - (items.size() - 1) * itemMargin;
        }
        float x = 0;
        float y = 0;
        for(int i = 0; i < items.size(); i ++){
            Item item = items.get(i);
            mArea.set(start, (int)Math.round(mPaint.getStrokeWidth() / 2), start + items.get(i).width,
                    itemHeight - (int)Math.round(mPaint.getStrokeWidth() / 2));
            if(item.status)
                mPaint.setStyle(Paint.Style.FILL);
            else
                mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(mArea, itemHeight / 2, itemHeight / 2, mPaint);
            item.mRect.left = start;
            item.mRect.right = start + item.width;
            item.mRect.top = 0;
            item.mRect.bottom = itemHeight;
            x = (item.mRect.left + item.mRect.right) / 2;
            y = -mMetrics.top + vSpacePadding;
            textPaint.getTextBounds(item.text, 0, item.text.length(), mRect);
            canvas.drawText(item.text, x, y, textPaint);
            start += items.get(i).width + itemMargin;
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                tx = event.getX();
                ty = event.getY();
                Log.e(TAG, "tx = " + tx + ", ty = " + ty);
                currentItem = getClickedItem(tx, ty);
                if(currentItem != null){
                    outside = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                tx = event.getX();
                ty = event.getY();
                Log.e(TAG, "tx = " + tx + ", ty = " + ty);
                if(inItem(tx, ty, currentItem)) {
                    return true;
                }
                outside = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                tx = event.getX();
                ty = event.getY();
                Log.e(TAG, "tx = " + tx + ", ty = " + ty);
                if(inItem(tx, ty, currentItem) && !outside) {
                    resetSelectedLabels(currentItem);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setLabel(String[] label1){
        this.label = label1;
        items.clear();
        for(int i = 0; i < label.length; i++){
            Item item = new Item(label[i]);
            items.add(item);
        }
    }

    public String getSelectedItems(){
        if(selectedLabels == null || selectedLabels.isEmpty())
            return "";
        else {
            StringBuilder builder = new StringBuilder();
            for(Item item : selectedLabels)
                builder.append(item.text);
            return builder.toString();
        }
    }

    private void resetSelectedLabels(Item item){
        if(selectedLabels == null)
            selectedLabels = new ArrayList<>();
        Log.e(TAG, "item.text = " + item.text);
        if(item == null) return;
        for(int i = 0; i < selectedLabels.size(); i ++){
            Item label = selectedLabels.get(i);
            if(item.text.equals(label.text)){
                selectedLabels.remove(i);
                label.status = false;
                invalidate();
                return;
            }
        }
        item.status = true;
        selectedLabels.add(item);
        invalidate();
    }

    private boolean inItem(float x, float y, Item item){
        if(null == item) return false;
        Rect rect = item.mRect;
        if(rect.left < x && rect.right > x &&
                rect.top < y && rect.bottom > y)
            return true;
        return false;
    }

    private Item getClickedItem(float x, float y){
        for(int i = 0; i < items.size(); i ++){
            Rect rect = items.get(i).mRect;
            if(rect.left < x && rect.right > x &&
                    rect.top < y && rect.bottom > y)
                return items.get(i);
        }
        return null;
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, defStyleRes);
        textColor = array.getColor(R.styleable.LabelView_textColor, DEFAULT_TEXT_COLOR);
        textSize = array.getDimensionPixelSize(R.styleable.LabelView_textSize, DEFAULT_TEXT_SIZE);
        hSpacePadding = array.getDimensionPixelSize(R.styleable.LabelView_hSpacePadding, 0);
        vSpacePadding = array.getDimensionPixelSize(R.styleable.LabelView_vSpacePadding, 0);
        minItemWidth = array.getDimensionPixelSize(R.styleable.LabelView_minItemWidth, 0);
        itemMargin = array.getDimensionPixelSize(R.styleable.LabelView_itemMargin, 0);
        gravity = array.getInt(R.styleable.LabelView_gravity, 1);
        array.recycle();
    }

    private void initPaint(){
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        mMetrics = new TextPaint.FontMetrics();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(Utils.dip2px(getContext(), 1));
        mPaint.setColor(DEFAULT_BG_COLOR);
        mPaint.setAntiAlias(true);
    }

    private int measureWidth(int measureSpec){
        int width = getMinimumWidth();
        int widthMode = MeasureSpec.getMode(measureSpec);
        int widthSize = MeasureSpec.getSize(measureSpec);
        int tempWidth = getPaddingLeft() + getPaddingRight();
        for(Item item : items){
            item.width =  Math.max(minItemWidth, (int)(2 * hSpacePadding + textPaint.measureText(item.text)));
            tempWidth += item.width;
        }
        itemsWidth = tempWidth;
        tempWidth += (VIRTUAL_COUNT - items.size()) * items.get(items.size() - 1).width;
        if(widthMode == MeasureSpec.EXACTLY){
            if(widthSize - width > 0)
                itemMargin = (widthSize - tempWidth) / (VIRTUAL_COUNT - 1);
            return Math.max(width, widthSize);
        }else if(widthMode == MeasureSpec.AT_MOST){
            return Math.min(Math.max(width, widthSize), tempWidth);
        }else {
            return Math.max(width, tempWidth);
        }
    }

    private int measureHeight(int measureSpec){
        int height = getMinimumHeight();
        int heightMode = MeasureSpec.getMode(measureSpec);
        int heightSize = MeasureSpec.getSize(measureSpec);
        if(heightMode == MeasureSpec.EXACTLY)
            return Math.max(height, heightSize);
        else {
            textPaint.getFontMetrics(mMetrics);
            itemHeight = (int)Math.ceil((mMetrics.bottom - mMetrics.top) + 2 * vSpacePadding + 2 * mPaint.getStrokeWidth());
            if(heightMode == MeasureSpec.AT_MOST){
                return Math.min(itemHeight, heightSize);
            }else {
                return itemHeight;
            }
        }
    }

    public static class Item{
        public String text;
        public boolean status;
        public Rect mRect;
        public int width;
        public int height;

        public Item(String text){
            this.text = text;
            this.status = false;
            this.mRect = new Rect();
        }
    }

}
