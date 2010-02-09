
package in.hotchpotch.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import android.os.Handler;

import android.util.Log;

import android.view.View;

public class ImageLoader extends View {
    private static final String TAG = "ImageLoader";
    private Bitmap image;
    private String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";
    private String lastPhotoPath = null;
    
    public ImageLoader(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        // Resources r = context.getResources();
        updateImage();
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer(true);
        final Handler handler = new Handler();
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    handler.post( new Runnable(){
                        public void run(){
                            // Log.d(TAG, "update image");
                            ImageLoader.this.updateImage();
                        }
                    });
                }
            }, 0, 1000);
    }

    private void updateImage() {
        String str = this.getPhotoPath();
        // Log.d(TAG, str);
        if (!str.equals(lastPhotoPath)) {
            // Log.d(TAG, "UPDATTTTTTTTTTTTTT");
            lastPhotoPath = str;
            InputStream fis = null;
            try {
                fis = new BufferedInputStream(new FileInputStream(str));
                image = BitmapFactory.decodeStream(fis);
                this.invalidate();
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
    }
// Photos/0316.jpg

    private String getPhotoPath() {
        Calendar cal = Calendar.getInstance();
        return String.format("%sPhotos/%02d%02d.jpg", 
                BISEI_APP_DIR, 
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
        );
    }

  @Override
      protected void onDraw(Canvas canvas) {
          canvas.drawBitmap(image,0,0,null);
          // int w = image.getWidth();
          // int h = image.getHeight();
          // Rect src = new Rect(0,0,w,h);
          // Rect dst = new Rect(0,200,w/2,200+h/2);
          // canvas.drawBitmap(image, src, dst, null);
      }
}


