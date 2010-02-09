
package in.hotchpotch.android;

import android.app.Activity;

import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;

public class YABiseiTokei extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // setContentView(R.layout.main);
        setContentView(new ImageLoader(this));
    }
}

