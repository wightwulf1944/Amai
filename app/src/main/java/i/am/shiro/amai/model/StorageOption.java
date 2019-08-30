package i.am.shiro.amai.model;

import android.content.Context;
import android.os.Environment;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.List;

import static com.annimon.stream.Collectors.toList;

public class StorageOption {

    private final String path;

    private final boolean isMounted;

    private final long spaceFree;

    private final int percentUsed;

    private StorageOption(File dir) {
        path = dir.getPath();

        String state = Environment.getStorageState(dir);
        isMounted = Environment.MEDIA_MOUNTED.equals(state);

        spaceFree = dir.getFreeSpace();

        long spaceTotal = dir.getTotalSpace();
        long spaceUsed = spaceTotal - spaceFree;
        percentUsed = (int) (spaceUsed / (spaceTotal / 100d));
    }

    public String getPath() {
        return path;
    }

    public boolean isMounted() {
        return isMounted;
    }

    public long getSpaceFree() {
        return spaceFree;
    }

    public int getPercentUsed() {
        return percentUsed;
    }

    public static List<StorageOption> getList(Context context) {
        File[] externalDirs = context.getExternalFilesDirs(null);
        return Stream.of(externalDirs)
            .map(StorageOption::new)
            .collect(toList());
    }
}
