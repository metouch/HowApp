package th.widget.easierpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.how.app.R;

/**
 * Created by me_touch on 18-3-8.
 *
 */

public class LinearIndicator implements Indicator<List>{

    private int selectedPosition = -1;

    private Context context;
    private DisplayMetrics metrics;
    private View indicatorParent;
    private LinearLayout indicatorContainer;
    private TextView tvMessage;
    private List datas;
    private List<View> indicators = new ArrayList<>();
    private EasierCarousel mCarousel;

    public LinearIndicator(@NonNull List datas){
        this.datas = datas;
    }

    @Override
    public List getDataSet() {
        return null;
    }

    @Override
    public ViewPager addPager(EasierCarousel parent) {
        ViewPager pager = new ViewPager(parent.getContext());
        EasierCarousel.LayoutParams lp = new EasierCarousel.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 360);
        pager.setLayoutParams(lp);
        return pager;
    }

    @Override
    public EasierCarousel.OnViewCreateAction getAction() {
        return null;
    }

    @Override
    public void bindEasierCarousel(EasierCarousel carousel) {
        this.mCarousel = carousel;
        carousel.setIndicator(this);
        carousel.initByIndicator();
    }

    @Override
    public int getIndicatorSize() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void notifyDataSetChanged() {
        if(mCarousel != null){
            fillIndicators();
            mCarousel.notifyDataSetChanged();
        }
    }

    @Override
    public View addIndicatorView(ViewGroup parent) {
        if(indicatorParent == null) {
            context = parent.getContext();
            metrics = context.getResources().getDisplayMetrics();
            indicatorParent = LayoutInflater.from(context)
                    .inflate(R.layout.vp_linear_vertical_indicator, parent, false);
            indicatorContainer = (LinearLayout) indicatorParent.findViewById(R.id.vp_linear_container);
            tvMessage = (TextView) indicatorParent.findViewById(R.id.vp_linear_text);
            fillIndicators();
        }
        return indicatorParent;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("position = " + position);
        int size = indicators.size();
        if(selectedPosition >= 0 && selectedPosition <size){
            indicators.get(selectedPosition).setSelected(false);
        }
        if(position >= 0 && position < size) {
            indicators.get(position).setSelected(true);
        }
        selectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void fillIndicators(){
        if(context == null){
            return;
        }
        int expect = getIndicatorSize();
        int actual = indicatorContainer.getChildCount();
        for(int i = actual; i > expect; i --){
            indicatorContainer.removeViewAt(i - 1);
            indicators.remove(i - 1);
        }
        for(int i = actual; i < expect; i ++){
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.vp_indicator);
            indicatorContainer.addView(imageView, generateLp(i != 0 ? 10 : 0));
            indicators.add(imageView);
        }
    }

    private LinearLayout.LayoutParams generateLp(int leftMargin){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftMargin, metrics);
        return lp;
    }
}
