package i.am.shiro.amai;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Shiro on 1/5/2018.
 */

public class AmaiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    String tag = super.createStackElementTag(element);
                    String method = element.getMethodName();
                    return String.format("%s:%s", tag, method);
                }
            });
        }

        Preferences.init(this);
    }
}
