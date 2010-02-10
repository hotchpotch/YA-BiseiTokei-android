
package in.hotchpotch.android.yabiseitokei;

import java.io.File;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;

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
    private Timer timer;
    private boolean dirExists;
    public static String TAG = "YABiseiTokei";
    public static String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";

    @Override
    public void onCreate(Bundle iCicle) {
        super.onCreate(iCicle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dirExists = (new File(BISEI_APP_DIR)).exists();
        if (dirExists) {
            // setContentView(R.layout.main);
            mImageLoader = new ImageLoader(this);
            setContentView(mImageLoader);
        } else {
            new AlertDialog.Builder(this)
               .setMessage(String.format(getString(R.string.dir_not_found), BISEI_APP_DIR))
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        YABiseiTokei.this.finish();
                    }
            })
            .create()
            .show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dirExists) {
            startTimer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        timerCancel();
    }

    private void startTimer() {
        timerCancel();
        timer = new Timer(true);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String time = (String) msg.obj;
                Log.i(TAG, String.format("updateHandler - %s", time));
                mImageLoader.updateImage(time);
                playVoice(time);
            }
        };
        timer.schedule(new ImageUpdateTask(handler), 0, 1000);
    }

    private void timerCancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        };
    }

    private void playVoice(String time) {
        File file = new File(String.format("%sSounds/Time/%s.mp3", YABiseiTokei.BISEI_APP_DIR, time));
        Uri uri = Uri.parse(file.toURI().toString());
        MediaPlayer mp = MediaPlayer.create(this, uri);
        mp.start();
    }
}

