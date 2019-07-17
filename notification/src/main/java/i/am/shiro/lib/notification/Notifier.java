package i.am.shiro.lib.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

public class Notifier {

    final Context context;

    final int id;

    public Notifier(@NonNull Context context, int id) {
        this.context = context;
        this.id = id;
    }

    public void show(@NonNull Notice notice) {
        show(context, id, notice);
    }

    public void cancel() {
        cancel(context, id);
    }

    public static void show(@NonNull Context context, int id, @NonNull Notice notice) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, notice.toNotification(context));
    }

    public static void cancel(@NonNull Context context, int id) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancel(id);
    }
}
