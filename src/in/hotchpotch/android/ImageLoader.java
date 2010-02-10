
package in.hotchpotch.android;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Calendar;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.os.Handler;
import android.os.Message;

import android.util.Log;

import android.view.View;

public class ImageLoader extends View {
    private static final String TAG = "ImageLoader";
    private Bitmap mImage;
    private String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";
    private String mLastPhotoPath = null;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    
    public ImageLoader(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer(true);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                ImageLoader.this.updateImage((String) msg.obj);
            }
        };
        timer.schedule(new ImageUpdateTask(handler), 0, 1000);
    }

    private void updateImage(String time) {
        mImage = null;
        final String path = getPhotoPath(time);
        mExecutor.execute(new Runnable() { public void run() {
            if (Thread.interrupted()) {
                return;
            }
            InputStream fis = null;
            try {
                fis = new BufferedInputStream(new FileInputStream(path));
                mImage = BitmapFactory.decodeStream(fis);
                post(new Runnable() { 
                    public void run() {
                        invalidate();
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, String.format("error file, %s", e.getMessage()), e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        Log.e(TAG, String.format("error file, %s", e.getMessage()), e);
                    }
                }
            }
        }
        });
    }

    private String getPhotoPath(String time) {
        return String.format("%sPhotos/%s.jpg", BISEI_APP_DIR, time);
    }

  @Override
      protected void onDraw(final Canvas canvas) {
          if (mImage != null) {
              canvas.drawBitmap(mImage,0,0,null);
          }
      }
}


