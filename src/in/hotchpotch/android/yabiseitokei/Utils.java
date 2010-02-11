
package in.hotchpotch.android.yabiseitokei;

public class Utils {
    public static int detectAct(String hhmm) {
        int hh = 12 - (Integer.parseInt(hhmm.substring(0, 2)) % 12);
        int m = Integer.parseInt(hhmm.substring(2, 4)) / 5;

        return (m + hh) % 12;
    }
}


