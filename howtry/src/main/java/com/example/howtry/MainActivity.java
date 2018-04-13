package com.example.howtry;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Uri uri = android.net.Uri.parse("content://th.how.app.provider" + "/NEWS_ENTITY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dbOperation();
            }
        }, 5000);
    }

    private void dbOperation(){

        String[] projection = new String[]{"_id",
                "TITLE", "SUB_TITLE"};

        String selection = "TITLE LIKE ?";

        String[] selectionArgs = new String[]{"标题%"};

        String sortOrder = "SUB_TITLE";
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        while (cursor.moveToNext()){
            Log.d("MainActivity", "id = " + cursor.getLong(0) + ", title = " + cursor.getString(1) + ", subtitle = " + cursor.getString(2));
        }
    }
}
