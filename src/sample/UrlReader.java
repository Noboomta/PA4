package sample;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlReader {
    final int BUFFERSIZE = 16*1024;
    public String urlName;
    public String outName;
    public long length;
    public URLConnection connection;

    public long geLength(URL url){
        long length = 0;
        URLConnection connection = null;
        try {
            connection = url.openConnection( );
            length = connection.getContentLengthLong( );
        } catch (MalformedURLException ex) {
            // URL constructor may throw this
        } catch (IOException ioe) {
            // getContentLengthLong may throw IOException
        }
        return length;
    }

    public void Read(String urlName, long size, String outName) throws IOException {
        urlName = urlName;
        this.urlName = "http://www.ku.ac.th/index.html";
        this.outName = outName;

        URL url = null;
        try {
            url = new URL( this.urlName );
        } catch ( MalformedURLException e ) {
            System.err.println( e.getMessage() );
            System.out.println("ERror line 39");
        }
        this.length = this.geLength(url);

        InputStream in = this.connection.getInputStream( );
        File file = null;
        OutputStream out = getOutputStream( file );
        byte[] buffer = new byte[BUFFERSIZE];
        try {
            do {
                int n = in.read( buffer );
                if (n < 0) break; // n < 0 means end of the input
                out.write(buffer, 0, n); // write n bytes from buffer
//                bytesRead += n;
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

    }
    public OutputStream getOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

}
