package th.widget.easierpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import static th.widget.easierpager.EasierCarousel.*;

/**
 * Created by me_touch on 18-3-5.
 * @author me_touch
 */

public abstract class EasierAdapter extends PagerAdapter{

    private int cycleStrategy = CYCLE_STRATEGY_NO;

    private Indicator mIndicator;
    private SparseArray<View> views = new SparseArray<>();

    public void setIndicator(Indicator mIndicator) {
        this.mIndicator = mIndicator;
    }

    public void setCycleStrategy(int cycleStrategy) {
        this.cycleStrategy = cycleStrategy;
    }

    public SparseArray<View> getViews() {
        return views;
    }

    @Override
    public void notifyDataSetChanged() {
        synchronized (this) {
            views.clear();
            super.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int size = mIndicator == null ? 0 : mIndicator.getIndicatorSize();
        return size < 2 ? size :
               cycleStrategy == CYCLE_STRATEGY_BORDER ? size + 2:
               cycleStrategy == CYCLE_STRATEGY_MAX ? Integer.MAX_VALUE :
               size;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = createAndBindView(views, position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(object instanceof View){
            View view = (View)object;
            container.removeView(view);
            position = position / getCount();
            views.append(position, view);
        }
    }

    public abstract View createAndBindView(SparseArray<View> views, int position);
}
