package i.am.shiro.amai.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static i.am.shiro.amai.constant.DownloadStatus.DEFAULT;
import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Shiro on 3/10/2018.
 */

@Retention(SOURCE)
@IntDef({DEFAULT, QUEUED, DONE, FAILED})
public @interface DownloadStatus {
    int DEFAULT = 0;
    int QUEUED = 1;
    int DONE = 2;
    int FAILED = 3;
}
