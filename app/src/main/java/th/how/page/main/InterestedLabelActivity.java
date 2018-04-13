package th.how.page.main;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import th.how.anim.Rotate3dAnimation;
import th.how.app.R;
import th.widget.easierpager.EasierCarousel;
import th.widget.easierpager.Indicator;
import th.widget.easierpager.LinearIndicator;

public class InterestedLabelActivity extends AppCompatActivity {

    private final static String TAG = "InterestedLabelActivity";
    private EasierCarousel carousel;
    private List<String> urls = new ArrayList<>();
    private Indicator<List> mIndicator;
    private View view;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            urls.add("http://pic2.sc.chinaz.com/files/pic/pic9/201803/zzpic10704.jpg");
            urls.add("http://pic.sc.chinaz.com/files/pic/pic9/201801/bpic5315.jpg");
            urls.add("http://pic.sc.chinaz.com/files/pic/pic9/201801/zzpic9564.jpg");
            urls.add("http://pic.sc.chinaz.com/files/pic/pic9/201802/bpic5625.jpg");
            urls.add("http://language.chinadaily.com.cn/images/attachement/jpg/site1/20180306/64006a47a40a1c0874e50f.jpg");
            mIndicator.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        view = findViewById(R.id.iv_rotate);
        view.setOnClickListener(mRotateTask);
//        view = findViewById(R.id.view);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e(TAG, "onTouch : event.getAction() = " + event.getAction());
//                if(event.getAction() == MotionEvent.ACTION_DOWN)
//                    return true;
//                else
//                    return false;
//            }
//        });
//        view = findViewById(R.id.view);
//        carousel = (EasierCarousel) findViewById(R.id.eaier_pager);
//        mIndicator = new LinearIndicator(urls){
//            @Override
//            public EasierCarousel.OnViewCreateAction getAction() {
//                return new EasierCarousel.OnViewCreateAction() {
//                    @Override
//                    public View onViewCreatedAndBind(SparseArray<View> views, final int position) {
//                        ImageView imageView;
//                        if(views.size() > 0){
//                            imageView = (ImageView)views.valueAt(0);
//                            views.removeAt(0);
//                        }else {
//                            imageView = new ImageView(InterestedLabelActivity.this);
//                        }
//                        Glide.with(imageView.getContext()).load(urls.get(position)).into(imageView);
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(InterestedLabelActivity.this, "" + position, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return imageView;
//                    }
//                };
//            }
//        };
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("InterestedLabelActivity", "onClick");
//            }
//        });
//        mIndicator.bindEasierCarousel(carousel);
//        carousel.postDelayed(runnable, 12000);
//        Path path = new Path();
//        Matrix matrix = new Matrix();

    }

    private View.OnClickListener mRotateTask = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Rotate3dAnimation animation = new Rotate3dAnimation(InterestedLabelActivity.this,
                    0 , 360, v.getWidth() / 2, v.getHeight() / 2, 0 , false);
            animation.setDuration(3000);
            animation.setFillAfter(true);
            animation.setInterpolator(new LinearInterpolator());
            v.startAnimation(animation);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
 //       carousel.removeCallbacks(runnable);
    }
}
