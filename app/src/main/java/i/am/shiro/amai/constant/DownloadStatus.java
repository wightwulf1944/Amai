package i.am.shiro.amai.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({
        DownloadStatus.QUEUED,
        DownloadStatus.RUNNING,
        DownloadStatus.PAUSED,
        DownloadStatus.DONE,
        DownloadStatus.FAILED
})
public @interface DownloadStatus {
    int QUEUED = 0;
    int RUNNING = 1;
    int PAUSED = 2;
    int DONE = 3;
    int FAILED = 4;
}
