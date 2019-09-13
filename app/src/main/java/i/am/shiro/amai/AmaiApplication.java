package i.am.shiro.amai;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

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
                .schemaVersion(1)
                .migration(new Migration())
                .compactOnLaunch()
                .build();
        Realm.setDefaultConfiguration(config);

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

        GlideBuilder glideBuilder = new GlideBuilder()
            .setLogLevel(Log.VERBOSE);
        Glide.init(this, glideBuilder);
    }
}
