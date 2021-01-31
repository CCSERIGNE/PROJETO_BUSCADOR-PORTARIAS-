package buscador_portarias;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class Downloaderteste {

    InputStream inputStream;
    OutputStream outputStream;
    URL url;
    int length;
    byte[] buffer = new byte[4096];

    public Boolean downloadUrl() {

        try {

            url = new URL("https://sippag.ifrs.edu.br/portarias/visualizar/?ano=2021&numero=1&hash=205EDB64F999F32D95B46ABD3900550B");
            inputStream = url.openStream();
            outputStream = new FileOutputStream("C:\\Users\\Igor\\Documents\\PDFs\\Novo\\aaaaaaa.pdf");

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                System.out.println(buffer);
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
    public static void main(String[] args) {
        Downloaderteste pp = new Downloaderteste();
        pp.downloadUrl();
    }
}
