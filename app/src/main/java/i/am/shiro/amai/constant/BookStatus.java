package i.am.shiro.amai.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static i.am.shiro.amai.constant.BookStatus.OFFLINE;
import static i.am.shiro.amai.constant.BookStatus.ONLINE;
import static i.am.shiro.amai.constant.BookStatus.QUEUED;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Shiro on 2/20/2018.
 */

@Retention(SOURCE)
@IntDef({ONLINE, QUEUED, OFFLINE})
public @interface BookStatus {
    int ONLINE = 0;
    int QUEUED = 1;
    int OFFLINE = 2;
}
