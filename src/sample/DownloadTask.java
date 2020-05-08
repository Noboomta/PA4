package sample;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task {
    private URL url;
    private File outfile;

    public DownloadTask(URL url, File outfile){
        this.url = url;
        this.outfile = outfile;
    }

    @Override
    public Long call() throws Exception {
        long bytesRead = 0;
        final int BUFFERSIZE = 16*1024;
        long fileSize = fileSize(url);
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream( );
        OutputStream out = new FileOutputStream(outfile);
        byte[] buffer = new byte[BUFFERSIZE];
        try {
            do {
                int n = in.read( buffer );
                if (n < 0) break; // n < 0 means end of the input
                out.write(buffer, 0, n); // write n bytes from buffer
                bytesRead += n;
                updateProgress(bytesRead, fileSize);
            } while ( true );
        }
        catch( IOException ex ) {
            // handle it
        }
        finally {
            //TODO add try-catch to each close()
            in.close();
            out.close();
        }
        return null;
    }

    public long fileSize(URL url){
        long length = 0;
        try {
            URLConnection connection = url.openConnection( );
            length = connection.getContentLengthLong( );
        } catch (MalformedURLException ex) {
            // URL constructor may throw this
        } catch (IOException ioe) {
            // getContentLengthLong may throw IOException
        }
        return length;
    }
}
