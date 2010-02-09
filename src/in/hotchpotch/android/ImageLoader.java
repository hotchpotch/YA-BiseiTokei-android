
package in.hotchpotch.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Calendar;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import android.util.Log;

import android.view.View;

public class ImageLoader extends View {
    private Bitmap image;
    private String BISEI_APP_DIR = "/sdcard/bisei-tokei/Payload/BiseiTokei.app/";
    
    public ImageLoader(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        // Resources r = context.getResources();
        InputStream fis = null;
        try {
            String str = this.getPhotoPath();
            // File file = new File(str);
            // Log.i("a", String.format("%b", file.canRead()));
            fis = new BufferedInputStream(new FileInputStream(str));
            image = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            Log.e("erro file", e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("erro file", e.getMessage(), e);
                }
            }
        }
    }
// Photos/0316.jpg

    private String getPhotoPath() {
        Calendar cal = Calendar.getInstance();
        return String.format("%sPhotos/%2d%2d.jpg", 
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


