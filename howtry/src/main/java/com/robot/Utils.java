package com.robot;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hasee on 2016/3/14.
 */
public class Utils {
    public static String files = Environment.getExternalStorageDirectory().getPath() + "/Image/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";

    public static String GetAdvertisingPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/gzccgg1.png";
    }

    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    public static String getDateToString2(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日    HH:mm");
        return sf.format(d);
    }

    public static String getDateToString5(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
        return sf.format(d);
    }

    public static String getDateToString6(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年");
        return sf.format(d);
    }

    public static String getDateToString7(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        return sf.format(d);
    }

    public static String getDateToString3(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(d);
    }

    public static String getDateToString4(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    public static String getDateToString3_yue(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM月");
        return sf.format(d);
    }

    public static String getDateToString3_ri(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("dd日");
        return sf.format(d);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    public static ArrayList<String> convertStrToArrayList(String str) {
        ArrayList<String> String = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] strArray = null;
            strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
            for (int i = 0; i < strArray.length; i++) {
                String.add(strArray[i]);
            }
        }
        return String;
    }

    public static String[] convertStrToArray2(String str) {
        String[] strArray = null;
        strArray = str.split("|"); //拆分字符为"|" ,然后把结果交给数组strArray
        return strArray;
    }

    public static void LayoutParams4_3(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 4 * 3;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams4_3_padding20(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = (getWith(context) - dip2px(context, 20)) / 4 * 3;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams16_10(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 16 * 10;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams16_9(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 16 * 9;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams23_10(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 23 * 10;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams2_1(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 2 * 1;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams3_1(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = getWith(context) / 3 * 1;

        imageView.setLayoutParams(para);
    }

    public static void LayoutParams2_1_padding20(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = (getWith(context) - px2dip(context, 20)) / 2 * 1;
        imageView.setLayoutParams(para);
    }
    public static void LayoutParams16_9_padding20(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = (getWith(context) - px2dip(context, 20)) / 16 * 9;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams2_1_padding40(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = (getWith(context) - px2dip(context, 100)) / 2 * 1;
        imageView.setLayoutParams(para);
    }

    public static void LayoutParams2_1_padding20list(Context context, View imageView) {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.height = (getWith(context) / 2 - px2dip(context, 40)) / 2 * 1;
        imageView.setLayoutParams(para);
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getWith(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    public static String saveMyBitmap(Bitmap mBitmap) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/pai" + System.currentTimeMillis() + ".png";
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
        int nian = Integer.parseInt(getDateToString7(t));
        int nownian = Integer.parseInt(getDateToString7(System.currentTimeMillis()));
        if (day - 365 >= 0) {
            sb.append(getDateToString6(t));
        }else if (day - 2 > 0) {
//            sb.ppend(day + "天");
            sb.append(getDateToString5(t));
        } else if (hour - 1 > 0) {
            if (hour > 24) {
//                sb.append("昨天");
                sb.append(getDateToString5(t));
            } else {
                sb.append(hour + "小时前");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时前");
            } else {
                sb.append(minute + "分钟前");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟前");
            } else {
                sb.append(mill + "秒前");
            }
        } else {
            sb.append("刚刚");
        }
//        if (!sb.toString().equals("刚刚")) {
//            sb.append("前");
//        }
        return sb.toString();
    }


    public static Bitmap createVideoThumbnail(String filePath) {
        // MediaMetadataRetriever is available on API Level 8
        // but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();

            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);

            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } catch (InstantiationException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (InvocationTargetException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (ClassNotFoundException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (NoSuchMethodException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } catch (IllegalAccessException e) {
            Log.e("TAG", "createVideoThumbnail", e);
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    public static String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = -1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
            System.out.println("versionCode-->" + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getAppVersionName(Context context) {
        String versionCode = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionName;
            System.out.println("versionCode-->" + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private static final String FILE_NAME = "gzcc_share.png";
    public static String ShareLogo(Context c) {
        String IMAGE_PATH = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                && Environment.getExternalStorageDirectory().exists()) {
            IMAGE_PATH = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + FILE_NAME;
        } else {
            IMAGE_PATH = c.getFilesDir().getAbsolutePath()
                    + File.separator + FILE_NAME;
        }
        return IMAGE_PATH;
    }

    public static Bitmap createVideoThumbnail1(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    static CharSequence charSequence;

    //是否连接WIFI
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public static String getAppversionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     *@param
     *@描述  快速模糊化处理bitmap
     *@作者  tll
     *@时间  2016/12/5 19:22
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
