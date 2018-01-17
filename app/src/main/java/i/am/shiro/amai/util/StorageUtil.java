package i.am.shiro.amai.util;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;

import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedFunction;
import com.annimon.stream.function.Predicate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import i.am.shiro.amai.model.StorageOption;

import static com.annimon.stream.Collectors.toList;

/**
 * Created by Shiro on 1/16/2018.
 */

public class StorageUtil {

    public static List<StorageOption> getStorageOptions(Context context) {
        ArrayList<StorageOption> storageOptions = new ArrayList<>();
        storageOptions.add(getInternalStorageOption(context));
        storageOptions.addAll(getExternalStorageOptions(context));
        return storageOptions;
    }

    private static StorageOption getInternalStorageOption(Context context) {
        File internalDir = context.getFilesDir();
        return new StorageOption("Internal", internalDir);
    }

    private static List<StorageOption> getExternalStorageOptions(Context context) {
        File[] externalDirs = ContextCompat.getExternalFilesDirs(context, null);
        return Stream.of(externalDirs)
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
