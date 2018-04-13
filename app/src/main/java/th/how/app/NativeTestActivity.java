package th.how.app;

import android.database.AbstractCursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NativeTestActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_test);
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
