package i.am.shiro.amai.util;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;

import com.annimon.stream.Objects;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedFunction;
import com.annimon.stream.function.Predicate;

import java.io.File;
import java.util.List;

import i.am.shiro.amai.model.StorageOption;

import static com.annimon.stream.Collectors.toList;

public class StorageUtil {

    public static List<StorageOption> getStorageOptions(Context context) {
        File[] externalDirs = ContextCompat.getExternalFilesDirs(context, null);
        return Stream.of(externalDirs)
                .filter(Objects::nonNull)
                .filter(isMounted())
                .mapIndexed(externalStorageOptionFrom())
                .collect(toList());
    }

    private static Predicate<File> isMounted() {
        return file -> {
            String state = EnvironmentCompat.getStorageState(file);
            return Environment.MEDIA_MOUNTED.equals(state);
        };
    }

    private static IndexedFunction<File, StorageOption> externalStorageOptionFrom() {
        return (index, file) -> new StorageOption("External " + index, file);
    }
}
