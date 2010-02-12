
package in.hotchpotch.android.yabiseitokei;

import java.io.File;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;

import android.media.AudioManager;
import android.media.MediaPlayer;

import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import android.preference.PreferenceManager;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class YABiseiTokei extends Activity {
    public static final String TAG = "YABiseiTokei";
    public static final String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";

    private ImageLoader mImageLoader;
    private Timer timer;
    private boolean dirExists;

    private SharedPreferences mPrefs;
    private PowerManager.WakeLock mWakelock = null;

    @Override
    public void onCreate(Bundle iCicle) {
        super.onCreate(iCicle);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);     
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dirExists = (new File(BISEI_APP_DIR)).exists();
        if (dirExists) {
            setContentView(R.layout.main); // new ImageLoader(this);
            mImageLoader = (ImageLoader) findViewById(R.id.image); // new ImageLoader(this);
            // mImageLoader = new ImageLoader(this);
            // setContentView(mImageLoader);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        showInfo();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        hideInfo();
    }

    public void showInfo() {
        findViewById(R.id.info).setVisibility(View.VISIBLE);
    }

    public void hideInfo() {
        findViewById(R.id.info).setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.main_menu_settings:
            startActivity(new Intent(this, Settings.class));
            return true;
        case R.id.main_menu_timetone:
            playTimeTone(Utils.getNowHHMM(), false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dirExists) {
            startTimer();
        }
    }

    @Override
    public void onResume() {
        boolean wake = mPrefs.getBoolean("wakelock", true);
        if (wake) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            mWakelock.acquire();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mWakelock != null) {
            mWakelock.release();
            mWakelock = null;
        }
        super.onPause();
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
            @Override
			public void handleMessage(Message msg) {
                String time = (String) msg.obj;
                Log.i(TAG, "LLLLLLLLLLLLLLL");

                mImageLoader.updateImage(time);

                if (!mPrefs.getBoolean("ignore_silent", false)) {
                    // サイレント
                    if (isSilent()) {
                        return;
                    }
                }

                int actId = Utils.detectAct(time);
                boolean say = mPrefs.getBoolean(String.format("sayyou_%d", actId), true);
                Log.i(TAG, String.format("%s: ActID:%d Sayyou:%s Say?:%b", time, actId, getResources().getStringArray( R.array.acts )[actId], say));
                
                if (say) {
                    playVoice(time);
                }
            }
        };
        timer.schedule(new ImageUpdateTask(handler), 0, 1000);
    }

    private boolean isSilent() {
        int audioMode = ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
        return audioMode == AudioManager.RINGER_MODE_SILENT || audioMode == AudioManager.RINGER_MODE_VIBRATE;
    }

    private void timerCancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        };
    }

    // ToDo: 音声再生をキューに入れて直列に
    private void playVoice(String time) {
        String timetoneType = mPrefs.getString("time_tone", "serif_every");
        if (timetoneType == "none") {
            return;
        }
        boolean serifTime = (Integer.parseInt(time.substring(2, 4)) % 15 == 0) ? true : false;

        if (serifTime && timetoneType.indexOf("serif") != -1) {
            // Log.i(TAG, String.format("- %b - %s", serifTime, timetoneType));
            playSerif(time);
        } else {
            if (timetoneType.indexOf("every") != -1) {
                playTimeTone(time, false);
            }
        }
    }

    private void playSerif(String time) {
        File file = new File(String.format("%sSounds/Serif/%s.mp3", YABiseiTokei.BISEI_APP_DIR, time));
        if (file.canRead()) {
            Uri uri = Uri.parse(file.toURI().toString());
            MediaPlayer mp = MediaPlayer.create(this, uri);
            mp.start();
        } else {
            Log.e(TAG, String.format("serif file can't read! - %s", file.toString()));
        }
    }

    private void playTimeTone(final String time, boolean withSerif) {
        File file = new File(String.format("%sSounds/Time/%s.mp3", YABiseiTokei.BISEI_APP_DIR, time));
        Uri uri = Uri.parse(file.toURI().toString());
        MediaPlayer mp = MediaPlayer.create(this, uri);
        if (withSerif) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer _) {
                    playSerif(time);
                }
            });
        }
        mp.start();
    }
}

