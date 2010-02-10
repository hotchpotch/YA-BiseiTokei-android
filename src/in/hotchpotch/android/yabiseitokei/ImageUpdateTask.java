package in.hotchpotch.android.yabiseitokei;

import java.util.Calendar;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class ImageUpdateTask extends TimerTask {
    private String mLastTime;
    private Handler updateHandler;

    public ImageUpdateTask(Handler handler) {
        super();
        updateHandler = handler;
    }

    @Override
    public void run() {
        String currentTime = getHHMM();
        if (!currentTime.equals(mLastTime)) {
            mLastTime = currentTime;
            Message msg = new Message();
            msg.obj = mLastTime;
            updateHandler.sendMessage(msg);
        }
    }

    public String getHHMM() {
        Calendar cal = Calendar.getInstance();
        return String.format("%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }
}


