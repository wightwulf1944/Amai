package i.am.shiro.amai.model;

import io.realm.RealmObject;

public class DownloadTask extends RealmObject {

    private DownloadJob parentJob;

    private String sourceUrl;

    private String destinationUrl;

    public DownloadJob getParentJob() {
        return parentJob;
    }

    public void setParentJob(DownloadJob parentJob) {
        this.parentJob = parentJob;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }
}
