package i.am.shiro.lib.notification;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Implement this and use {@link Notifier#show(Notice)}
 */
public interface Notice {

    @NonNull
    Notification toNotification(@NonNull Context context);
}
