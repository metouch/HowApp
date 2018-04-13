package th.widget.easierpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import th.how.app.R;


/**
 * Created by me_touch on 18-3-5.
 *
 * @author me_touch
 */

public class EasierCarousel extends FrameLayout {

    private final static String TAG = "EasierCarousel";
    public static final int LAYOUT_FRAME = 0;
    public static final int LAYOUT_LINEAR_VERTICAL = 1;

    public static final int CYCLE_STRATEGY_NO = 0;
    public static final int CYCLE_STRATEGY_MAX = 1;
    //多次notify操作,可能导致页面空白
    public static final int CYCLE_STRATEGY_BORDER = 2;


    private int mTotalHeight;
    private Rect mRect;
    //触摸时是否停止循环
    private boolean stopWhileTouch = false;
    //viewpager 与 indicator之间的布局模式
    private int layoutMode = LAYOUT_FRAME;
    //循环策略
    private int cycleStrategy;
    //检测adapter初始添加至ViewPager时,datas值是否为空
    private boolean isEmpty;
    //是否正在循环播放
    private boolean isCirculating = false;
    //定时, 为0时表示不轮播,单位毫秒, 赋值时,为了避免过于频繁的播放, 间隔时间应大于200
    private int delayTime = 0;

    private Context mContext;
    private Indicator mIndicator;
    private View mIndicatorView;

    private Handler mHandler;
    private Runnable cycleRunnable = new Runnable() {
        @Override
        public void run() {
            int next = mViewPager.getCurrentItem();
            next += 1;
            mViewPager.setCurrentItem(next, true);
            allowCyclePlay();
        }
    };

    private ViewPager mViewPager;
    private OnViewCreateAction mViewAction;
    private EasierAdapter adapter = new EasierAdapter() {
        @Override
        public View createAndBindView(SparseArray<View> views, int position) {
            int size = mIndicator.getIndicatorSize();
            if (mViewAction == null)
                throw new NullPointerException("Field OnViewCreateAction can not be null");
            if(cycleStrategy == CYCLE_STRATEGY_BORDER && size > 1){
                if(position == 0){
                    position = size - 1;
                }else if(position == size + 1){
                    position = 0;
                }else {
                    position -= 1;
                }
            }else{
                position = position % size;
            }
            return mViewAction.onViewCreatedAndBind(views, position);
        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        int mappedPosition = 0;
        int actualPosition = 0;
        int selIndicatorPos = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG,"onPageSelected position = " + position);
            int size = mIndicator.getIndicatorSize();
            actualPosition = position;
            if (cycleStrategy == CYCLE_STRATEGY_BORDER) {
                if(position < 0 || position > size + 1) return;
                if(position == 0){
                    mappedPosition = size;
                    selIndicatorPos = size - 1;
                }else if(position == size + 1){
                    mappedPosition = 1;
                    selIndicatorPos = 0;
                }else {
                    selIndicatorPos = position - 1;
                }
                if(mIndicator != null && actualPosition != 0 && actualPosition != size + 1){
                    mIndicator.onPageSelected(selIndicatorPos);
                }
            }else {
                selIndicatorPos = position % size;
                mIndicator.onPageSelected(selIndicatorPos);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int size = mIndicator.getIndicatorSize();
            if(cycleStrategy == CYCLE_STRATEGY_BORDER){
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    if (actualPosition == 0 || actualPosition == size + 1) {
                        mViewPager.setCurrentItem(mappedPosition, false);
                        mIndicator.onPageSelected(mappedPosition - 1);
                    }
                }
            }
            if(mIndicator != null){
                mIndicator.onPageScrollStateChanged(state);
            }
        }
    };

    public EasierCarousel(Context context) {
        this(context, null);
    }

    public EasierCarousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasierCarousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public EasierCarousel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mIndicator != null && layoutMode == LAYOUT_LINEAR_VERTICAL){
            measureLinearVertical(widthMeasureSpec, heightMeasureSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(mIndicator != null && layoutMode == LAYOUT_LINEAR_VERTICAL){
            layoutVertical(changed, left, top, right, bottom);
        }else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(getChildCount() > 2)
            throw new IllegalArgumentException("child's num can not be larger than 2");
        super.addView(child, index, params);
    }

    /**
     * 与LinearLayout的测量原理相似
     * @param widthMeasureSpec spec
     * @param heightMeasureSpec spec
     */
    private void measureLinearVertical(int widthMeasureSpec, int heightMeasureSpec){
        mTotalHeight = 0;
        int maxWidth = 0;
        int tempWidth = 0;
        int childState = 0;
        boolean allFillParent = true;
        boolean matchParent = false;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        for(int i = 0; i < getChildCount(); i ++){
            View child = getChildAt(i);
            if(child == null){
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, mTotalHeight);
            mTotalHeight += (child.getMeasuredHeight() +lp.topMargin + lp.bottomMargin);

            allFillParent = allFillParent && lp.width == LayoutParams.MATCH_PARENT;
            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            if(widthMode != MeasureSpec.EXACTLY && lp.width == LayoutParams.MATCH_PARENT){
                tempWidth = Math.max(tempWidth, margin);
                matchParent = true;
            }else {
                tempWidth = Math.max(tempWidth, measuredWidth);
            }
            maxWidth = Math.max(maxWidth, measuredWidth);
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        if(!allFillParent && widthMode != MeasureSpec.EXACTLY){
            maxWidth = tempWidth;
        }
        mTotalHeight += (getPaddingTop() + getPaddingBottom());
        maxWidth += (getPaddingLeft() + getPaddingRight());
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(mTotalHeight, heightMeasureSpec, childState) & MEASURED_SIZE_MASK);
        if(matchParent){
            final int newSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
            for (int i = 0; i < getChildCount(); i ++){
                View child = getChildAt(i);
                if(child == null){
                    continue;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if(lp.width == LayoutParams.MATCH_PARENT && child.getVisibility() != GONE){
                    int oldHeight = lp.height;
                    measureChildWithMargins(child, newSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    private void layoutVertical(boolean changed, int left, int top, int right, int bottom){
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        int childTop = getPaddingTop();
        int childLeft;
        int width = right - left;
        int childSpace = width - paddingLeft - paddingRight;
        for (int i = 0; i < getChildCount(); i ++){
            View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            if(child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int gravity = params.gravity;
            if(gravity < 0){
                gravity = Gravity.LEFT;
            }
            switch (gravity){
                case Gravity.RIGHT:
                    childLeft = width - paddingRight - childWidth - params.rightMargin;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = paddingLeft + (childSpace - childWidth) / 2
                            + params.leftMargin - params.rightMargin;
                    break;
                case Gravity.LEFT:
                default:
                    childLeft = params.leftMargin + paddingLeft;
                    break;
            }
            childTop += params.topMargin;
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childTop += params.bottomMargin + childHeight;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mViewPager != null && stopWhileTouch){
            if(mRect == null){
                mRect = new Rect();
            }
            mRect.left = mViewPager.getLeft();
            mRect.right = mViewPager.getRight();
            mRect.top = mViewPager.getTop();
            mRect.bottom = mViewPager.getBottom();
            float x = ev.getX();
            float y = ev.getY();
            boolean inRange = x >= mRect.left && x <= mRect.right && y >= mRect.top && y <= mRect.bottom;
            if(inRange){
                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        stopCyclePlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        allowCyclePlay();
                        break;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }



    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
        stopCyclePlay();
        mHandler = null;
    }

    protected void setStopWhileTouch(boolean stopWhileTouch) {
        this.stopWhileTouch = stopWhileTouch;
    }

    protected void setCycleStrategy(int cycleStrategy) {
        this.cycleStrategy = cycleStrategy;
        adapter.notifyDataSetChanged();
    }

    protected void changeLayoutMode(int layoutMode){
        if(layoutMode <0 || layoutMode > 1) return;
        this.layoutMode = layoutMode;
        requestLayout();
    }

    protected void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        if(isCirculating) {
            mHandler.removeCallbacks(cycleRunnable);
        }
        allowCyclePlay();
    }

    public void setIndicator(@NonNull Indicator mIndicator) {
        this.mIndicator = mIndicator;
    }

    public void initViewPager(ViewPager pager) {
        if(pager == null) throw new NullPointerException("added pager cannot be null");
        if(pager.getParent() != null) throw new IllegalStateException("added pager cannot have a parent");
        if (this.mViewPager == null) {
            this.mViewPager = pager;
        }
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        adapter.setCycleStrategy(cycleStrategy);
        adapter.setIndicator(mIndicator);
        isEmpty = mIndicator.getIndicatorSize() < 2;
        mViewPager.setAdapter(adapter);
        setCurrentItem();
        ViewGroup.LayoutParams lp = mViewPager.getLayoutParams() == null
                ? new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                : mViewPager.getLayoutParams();
        addView(mViewPager, lp);
        addIndicator();
        allowCyclePlay();
    }

    private void setCurrentItem(){
        int size = mIndicator.getIndicatorSize();
        if(!isEmpty && cycleStrategy == CYCLE_STRATEGY_BORDER){
            mViewPager.setCurrentItem(1, false);
        }else if(!isEmpty && cycleStrategy == CYCLE_STRATEGY_MAX){
            mViewPager.setCurrentItem(1000 * size);
        }
    }

    private void addIndicator(){
        if(mIndicatorView != null && mIndicator.getIndicatorSize() > 0){
            ViewGroup.LayoutParams lp = mIndicatorView.getLayoutParams() == null
                    ? new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    : mIndicatorView.getLayoutParams();
            addView(mIndicatorView, lp);
        }
    }


    public void notifyDataSetChanged() {
        int size = mIndicator.getIndicatorSize();
        adapter.notifyDataSetChanged();
        if(mIndicatorView.getParent() == null){
            addIndicator();
        }
        if(cycleStrategy == CYCLE_STRATEGY_BORDER){
            if(isEmpty && size > 1){
                mViewPager.setCurrentItem(1, false);
                isEmpty = false;
            }
        }else if(cycleStrategy == CYCLE_STRATEGY_MAX){
            if(isEmpty && size > 1){
                int location = 1000 * size;
                Log.e(TAG, "location = " + location);
                mViewPager.setCurrentItem(location, false);
                isEmpty = false;
            }
        }
        if(!isCirculating){
            allowCyclePlay();
        }
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        mHandler = new Handler();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EasierCarousel, defStyleAttr, defStyleAttr);
        delayTime = array.getInt(R.styleable.EasierCarousel_delay_time, 0);
        stopWhileTouch = array.getBoolean(R.styleable.EasierCarousel_stop_while_touch, false);
        layoutMode = array.getInt(R.styleable.EasierCarousel_layout_mode, 0);
        cycleStrategy = array.getInt(R.styleable.EasierCarousel_cycle_strategy, 0);
        array.recycle();
    }

    private void allowCyclePlay(){
        if(cycleStrategy != CYCLE_STRATEGY_NO &&delayTime > 200
                && mIndicator.getIndicatorSize() > 1 && mHandler != null){
            isCirculating = true;
            mHandler.postDelayed(cycleRunnable, delayTime);
        }
    }

    private void stopCyclePlay(){
        isCirculating = false;
        mHandler.removeCallbacks(cycleRunnable);
    }

    public void initByIndicator() {
        if(mIndicator == null){
            return;
        }
//        mSettings = mIndicator.getSetting();
//        layoutMode = mSettings.getLayoutMode();
//        stopWhileTouch = mSettings.getStopWhileCycle();
//        delayTime = mSettings.getDelayTime();
//        cycleStrategy = mSettings.getCycleStrategy();
        mViewAction = mIndicator.getAction();
        mIndicatorView = mIndicator.addIndicatorView(this);
        assert mIndicatorView.getParent() == null : "indicatorView should not hava a parent";
        initViewPager(mIndicator.addPager(this));
    }

    public interface OnViewCreateAction {
        View onViewCreatedAndBind(SparseArray<View> views, int position);
    }
}
