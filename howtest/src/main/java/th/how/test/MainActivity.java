package th.how.test;

import android.content.Context;
import android.content.Intent;
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
        //grantUriPermission("th.how.test", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //dbOperation();
                Intent intent = new Intent();
                intent.setClassName("com.example.howtry", "com.example.howtry.MainActivity");
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        }, 10000);
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
