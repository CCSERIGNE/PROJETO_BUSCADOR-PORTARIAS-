package api.collection.portarias;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Serigne Khassim Mbaye 
 * Estudante Ciênça da computação
 * Email : serignekhassim@gmail.com
 * 19-09-2020 -  IBIRUBA - RS IFRS
 */


public class Downloader {

    InputStream inputStream;
    OutputStream outputStream;
    URL url;
    int length;
    byte[] buffer = new byte[4096];

    public Boolean downloadUrl(String imageUrl, String filename) {

        try {
            
            url = new URL(imageUrl);
            inputStream = url.openStream();
            outputStream = new FileOutputStream(filename);

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return true;
        } catch (MalformedURLException e) {

            System.out.println("MalformedURLException " + e.getMessage());
            return false;

        } catch (FileNotFoundException e) {

            System.out.println("FileNotFoundException " + e.getMessage());
            return false;

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
            return false;
        }
    }
}
