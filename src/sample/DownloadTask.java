package sample;

import javafx.concurrent.Task;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Download class Task to set myTask.
 */
public class DownloadTask extends Task {
    private URL url;
    private File outfile;
    private long start;
    private long size;

    /**
     * Download Task method.
     */
    public DownloadTask(URL url, File outfile, long start, long size) {
        this.url = url;
        this.outfile = outfile;
        this.start = start;
        this.size = size;
    }

    /**
     * Call method
     */
    @Override
    public Long call() {
        URLConnection conn = null;
        String range = null;
        final int BUFFERSIZE = 16 * 1024;
        byte[] buffer = new byte[BUFFERSIZE];
        long bytesRead = 0;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (size > 0) {
            range = String.format("bytes=%d-%d", start, start + size - 1);
        } else {
            // size not given, so read from start byte to end of file
            range = String.format("bytes=%d-", start);
        }
        conn.setRequestProperty("Range", range);
        try (InputStream in = conn.getInputStream();
             RandomAccessFile out = new RandomAccessFile(outfile, "rwd");) {
            int n = 0;
            out.seek(start);
            while ((n = in.read(buffer)) >= 0) {// n < 0 means end of the input
                out.write(buffer, 0, n); // write n bytes from buffer
                bytesRead += n;
                updateProgress(bytesRead, size);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(start + "  " + size);
        }
        return null;
    }

}
