
package in.hotchpotch.android.yabiseitokei;

import java.util.Calendar;

public class Utils {
    public static String getNowHHMM() {
        return calToHHMM(Calendar.getInstance());
    }

    public static String calToHHMM(Calendar cal) {
        return String.format("%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    public static int detectAct(String hhmm) {
        int hh = 12 - (Integer.parseInt(hhmm.substring(0, 2)) % 12);
        int m = Integer.parseInt(hhmm.substring(2, 4)) / 5;

        return (m + hh) % 12;
    }
}


