package i.am.shiro.amai;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Shiro on 1/8/2018.
 */

public class Preferences {

    private static final String IS_FIRST_RUN_KEY = "isFirstRun";

    public static boolean isFirstRun(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_FIRST_RUN_KEY, true);
    }

    public static void setFirstRunDone(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_FIRST_RUN_KEY, false)
                .apply();
    }
}
