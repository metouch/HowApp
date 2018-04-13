package com.robot;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by me_touch on 17-11-20.
 *
 */

public class MediaHelper {
    private MediaPlayer player;
    private ImageView ivAnim;
    private Context context;

    public MediaHelper(Context context){
        this.context = context;
    }

    public void play(ImageView ivAnim, String path){
        if(player != null){
            stop();
            player = null;
        }

        if(player == null){
            this.player = new MediaPlayer();
            this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            setListener();
        }
        if(ivAnim != null){
            this.ivAnim = ivAnim;
            Drawable drawable = ivAnim.getDrawable();
            if(drawable instanceof AnimationDrawable){
                ((AnimationDrawable)drawable).start();
            }
        }
        setDataSource(path);
        start();
    }

    public void setDataSource(String path){
        try {
            Log.e("MediaHelper", path);
            player.setDataSource(path);
        }catch (Exception e){
            toast("文件不存在或已损坏");
        }

    }

    public void setListener(){
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                MediaHelper.this.player.start();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                MediaHelper.this.player.reset();
                toast("播放出错");
                return false;
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
    }

    public void start(){
            player.prepareAsync();
    }

    public void stop(){
        player.stop();
        player.reset();
        player.release();
        player = null;
        if(ivAnim != null){
            Drawable drawable = ivAnim.getDrawable();
            if(drawable instanceof AnimationDrawable){
                ((AnimationDrawable)drawable).selectDrawable(0);
                ((AnimationDrawable)drawable).stop();
            }
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    private void toast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
