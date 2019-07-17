package i.am.shiro.amai.notification;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import i.am.shiro.amai.R;
import i.am.shiro.lib.notification.Notice;

import static i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID;

public final class ProgressNotice implements Notice {

    @NonNull
    @Override
    public Notification toNotification(@NonNull Context context) {
        return new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_download)
                .setContentTitle("Downloading")
                .setProgress(0, 0, true)
                .build();
    }
}
