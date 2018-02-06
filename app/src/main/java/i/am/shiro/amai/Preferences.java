package i.am.shiro.amai;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Shiro on 1/8/2018.
 */

public class Preferences {

    private static final String IS_FIRST_RUN = "isFirstRun";

    private static SharedPreferences sharedPreferences;

    static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isFirstRun() {
        return sharedPreferences
                .getBoolean(IS_FIRST_RUN, true);
    }

    public static void setFirstRun(boolean isFirstRun) {
        sharedPreferences
                .edit()
                .putBoolean(IS_FIRST_RUN, isFirstRun)
                .apply();
    }

    public static void setFirstRunDone() {
        setFirstRun(false);
    }
}
