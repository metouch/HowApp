package th.how.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import th.how.app.R;

/**
 * Created by hasee on 2016/4/6.
 */
public class ImageLoadUtils {

    public static void loadCircle(Context context, int resId, ImageView iv) {
        Glide.with(context)
                .load(resId)
                .crossFade()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .transform(new CircleTransform(context))
                .into(iv);
    }
    public static void loadCircle(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .transform(new CircleTransform(context))
                .into(iv);
    }
}
