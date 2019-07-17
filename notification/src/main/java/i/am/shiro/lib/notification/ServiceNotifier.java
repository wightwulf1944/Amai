package i.am.shiro.lib.notification;

import android.app.Service;

import androidx.annotation.NonNull;

public class ServiceNotifier extends Notifier {

    public ServiceNotifier(@NonNull Service service, int id) {
        super(service, id);
    }

    public void startForeground(@NonNull Notice notice) {
        startForeground((Service) context, id, notice);
    }

    public static void startForeground(@NonNull Service service, int id, @NonNull Notice notice) {
        service.startForeground(id, notice.toNotification(service));
    }
}
