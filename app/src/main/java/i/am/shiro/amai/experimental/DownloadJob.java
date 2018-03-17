package i.am.shiro.amai.experimental;

import i.am.shiro.amai.constant.DownloadStatus;
import i.am.shiro.amai.model.Book;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static java.lang.String.format;

/**
 * Created by Shiro on 3/17/2018.
 */

public class DownloadJob extends RealmObject {

    @PrimaryKey
    private int bookId;

    private Book parentBook;

    private String title;

    private int status;

    private int tries;

    private int taskIndex;

    private RealmList<DownloadTask2> taskList;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Book getParentBook() {
        return parentBook;
    }

    public void setParentBook(Book parentBook) {
        this.parentBook = parentBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DownloadStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(@DownloadStatus int status) {
        this.status = status;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public void incrementTries() {
        tries++;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public void incrementTaskIndex() {
        taskIndex++;
    }

    public RealmList<DownloadTask2> getTaskList() {
        return taskList;
    }

    public void setTaskList(RealmList<DownloadTask2> taskList) {
        this.taskList = taskList;
    }

    public String getStatusString() {
        switch (status) {
            case DownloadStatus.QUEUED:
                return "Queued";
            case DownloadStatus.RUNNING:
                return "Downloading";
            case DownloadStatus.PAUSED:
                return "Paused";
            case DownloadStatus.DONE:
                return "Done";
            case DownloadStatus.FAILED:
                return "Failed";
            default:
                throw new RuntimeException(format("Unrecognized status %s", status));
        }
    }
}
