
package in.hotchpotch.android;

import java.io.File;

import java.util.Timer;

import android.app.Activity;

import android.media.MediaPlayer;

import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;

import android.view.Window;
import android.view.WindowManager;

public class YABiseiTokei extends Activity {
    private ImageLoader mImageLoader;
    public static String TAG = "YABiseiTokei";
    public static String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";

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
                String time = (String) msg.obj;
                Log.i(TAG, String.format("updateHandler - %s", time));
                mImageLoader.updateImage(time);
                YABiseiTokei.this.playVoice(time);
            }
        };
        timer.schedule(new ImageUpdateTask(handler), 0, 1000);
    }

    private void playVoice(String time) {
        File file = new File(String.format("%sSounds/Time/%s.mp3", YABiseiTokei.BISEI_APP_DIR, time));
        Uri uri = Uri.parse(file.toURI().toString());
        MediaPlayer mp = MediaPlayer.create(this, uri);
        mp.start();
    }
}

