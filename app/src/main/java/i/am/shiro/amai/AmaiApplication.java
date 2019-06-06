package i.am.shiro.amai;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID;

public class AmaiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Preferences.init(this);

        if (BuildConfig.DEBUG) initDebugTools();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .compactOnLaunch()
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.compactRealm(config);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    DEFAULT_CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }

    private void initDebugTools() {
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(@NonNull StackTraceElement element) {
                String tag = super.createStackElementTag(element);
                String method = element.getMethodName();
                return String.format("%s:%s", tag, method);
            }
        });

//        Preferences.setFirstRun(true);
    }
}
