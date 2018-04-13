package th.widget.easierpager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by me_touch on 18-3-8.
 *
 */

public interface Indicator<T> extends ViewPager.OnPageChangeListener{

    T getDataSet();

    int getIndicatorSize();

    void notifyDataSetChanged();

    void bindEasierCarousel(EasierCarousel carousel);

    ViewPager addPager(EasierCarousel parent);

    View addIndicatorView(ViewGroup parent);

    EasierCarousel.OnViewCreateAction getAction();

}

