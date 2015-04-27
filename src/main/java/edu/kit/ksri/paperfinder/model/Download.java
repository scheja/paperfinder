package edu.kit.ksri.paperfinder.model;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.scholar.http.HttpClientFactory;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Created by janscheurenbrand on 28.01.15.
 */
public class Download implements Runnable {
    private String author;
    private String title;
    private long size;
    private long downloaded;
    private URI uri;
    private IntegerProperty status = new SimpleIntegerProperty();

    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR_NOTFOUND = 404;
    public static final int ERROR_UNKNOWN = 499;

    private static final int MAX_BUFFER_SIZE = 1024;

    public Download() {}

    public Download(Article article) {
        if (article.getPdfLink() != null) {
            this.title = article.getTitle();
            this.author = article.getAuthor();
            this.size = -1;
            this.downloaded = 0;
            try {
                this.uri = new URI(article.getPdfLink());
                this.status.set(DOWNLOADING);
                download();
            } catch (URISyntaxException e) {
                this.status.set(ERROR_NOTFOUND);
            }
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public int getStatus() {
        return status.get();
    }

    public IntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public String getFilename() {
        return (this.author + " - " + this.title).replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public void pause() {
        status.set(PAUSED);
    }

    public void resume() {
        status.set(DOWNLOADING);
        download();
    }

    public void cancel() {
        status.set(CANCELLED);
    }

    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private File getFile() {
        String uri = this.uri.toString();
        String extension = uri.contains(".") ? uri.substring(uri.lastIndexOf(".")) : ".pdf";
        return new File(Config.CONFIG_PATH + getFilename() + extension);
    }

    private void open() {
        if (this.status.get() == COMPLETE) {
            Platform.runLater(() -> {
                try {
                    Desktop.getDesktop().open(getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Download file
    public void run() {
        InputStream remoteContentStream = null;
        OutputStream localFileStream = null;

        if (Config.TEST_MODE) {
            int millis = new Random().nextInt(Config.SLEEP_WIGGLE) + Config.SLEEP_BASE;

            try {
                Thread.sleep(millis);
                Platform.runLater(() -> setStatus(COMPLETE));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                HttpClient httpClient = HttpClientFactory.getHttpClient();
                HttpGet httpGet = new HttpGet(this.uri);
                HttpResponse response = httpClient.execute(httpGet);
                remoteContentStream = response.getEntity().getContent();
                this.size = response.getEntity().getContentLength();

                File dir = getFile().getParentFile();
                dir.mkdirs();

                localFileStream = new FileOutputStream(getFile());
                byte[] buffer = new byte[MAX_BUFFER_SIZE];
                int sizeOfChunk;
                this.downloaded = 0;
                while ((sizeOfChunk = remoteContentStream.read(buffer)) != -1 && this.status.get() == DOWNLOADING) {
                    localFileStream.write(buffer, 0, sizeOfChunk);
                    this.downloaded += sizeOfChunk;
                }
                Platform.runLater(() -> setStatus(COMPLETE));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> setStatus(ERROR_UNKNOWN));
            } finally {
                try {
                    remoteContentStream.close();
                    localFileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleAction(ActionEvent actionEvent) {
        switch (this.status.get()) {
            case DOWNLOADING:
                cancel();
                break;
            case PAUSED:
                resume();
                break;
            case ERROR_NOTFOUND:
            case ERROR_UNKNOWN:
            case CANCELLED:
                download();
                break;
            case COMPLETE:
                open();
                break;
            default:
                break;
        }
    }
}
