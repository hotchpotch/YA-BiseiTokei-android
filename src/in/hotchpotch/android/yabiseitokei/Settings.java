
package in.hotchpotch.android.yabiseitokei;

import android.os.Bundle;

import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {
    public static final String TAG = "YABiseiTokeiSettings";

   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
   }
}

