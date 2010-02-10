
package in.hotchpotch.android;

import java.util.Timer;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.Window;
import android.view.WindowManager;

public class YABiseiTokei extends Activity {
    private ImageLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // setContentView(R.layout.main);
        mImageLoader = new ImageLoader(this);
        setContentView(mImageLoader);
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer(true);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                mImageLoader.updateImage((String) msg.obj);
            }
        };
        timer.schedule(new ImageUpdateTask(handler), 0, 1000);
    }
}

