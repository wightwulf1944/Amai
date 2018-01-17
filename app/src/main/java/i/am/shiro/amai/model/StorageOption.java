package i.am.shiro.amai.model;

import java.io.File;

/**
 * Created by Shiro on 1/15/2018.
 */

public class StorageOption {

    private final String title;

    private final String path;

    private final long freeSpace;

    private final long totalSpace;

    public StorageOption(String title, File dir) {
        this.title = title;
        path = dir.getPath();
        freeSpace = dir.getFreeSpace();
        totalSpace = dir.getTotalSpace();
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getFreeSpaceStr() {
        int freeSpaceMb = getFreeSpaceMb();
        if (freeSpaceMb < 1024) {
            return freeSpaceMb + " MB Free";
        } else {
            int freeSpaceGb = freeSpaceMb / 1024;
            return freeSpaceGb + " GB Free";
        }
    }

    public int getUsedSpaceMb() {
        return getTotalSpaceMb() - getFreeSpaceMb();
    }

    public int getTotalSpaceMb() {
        return getMbFromB(totalSpace);
    }

    public int getFreeSpaceMb() {
        return getMbFromB(freeSpace);
    }

    private int getMbFromB(long b) {
        return (int) ((b / 1024L) / 1024L);
    }
}
