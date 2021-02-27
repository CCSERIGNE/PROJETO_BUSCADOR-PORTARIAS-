/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.collection.portarias;

import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 *
 * @author sergi
 */
public class UFRGSNovo {

    static String _DEFAULT_URL = "https://www1.ufrgs.br/sistemas/sde/gerencia-documentos/index.php/publico/ExibirPDF?documento=";
    static int _THREADS = 10;
    public static int from_id;
    public static int to_id;
    static int increment = 0;

    public static boolean PreparedDownload(String webFile, String NameArchiv, int NumbeArchiv) {
        String[] convertHttps = webFile.split("http:");
        String getHttps;
//        System.out.println(" que: " + convertHttps[0]);
        if (convertHttps[0].isEmpty()) {
            getHttps = "https:" + convertHttps[1];
        } else {
            getHttps = webFile;
        }
        NameArchiv = getHttps;
        NameArchiv = NameArchiv.replace(":", "_DOISpont_");
        NameArchiv = NameArchiv.replace("//", "_baraduplas_");
        NameArchiv = NameArchiv.replace("/", "barra");
        NameArchiv = NameArchiv.replace("?", "interrogacao");

        getHttps = getHttps.replace("ç", "%C3%A7");
        getHttps = getHttps.replace("ã", "%C3%A3");
        getHttps = getHttps.replace("Ç", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");
        getHttps = getHttps.replace("Diário", "Di%C3%A1rio");
        getHttps = getHttps.replace("º", "%C2%BA");
        getHttps = getHttps.replace("?ano", "/?ano");

        String localpath = VarivaisGlobais.DEST + "" + NameArchiv + ".pdf";
        System.out.println("https : "+getHttps);
        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }

    public static  int download_incrementing() throws InterruptedException {
        int curent_id = from_id;
       
        Random gerador = new Random();
        while (from_id < to_id) {
            if (curent_id % 100 == 0) {
                System.out.println("cuent_id : " + curent_id);
            }
            increment = increment+1;
            String url = _DEFAULT_URL + String.valueOf(curent_id+increment);
           
//            gerador = (Random) new Random().doubles();
            float t = (float) 0.1;
            if (gerador.nextFloat() < t) {
                Thread.sleep(60);
            }
            try {
                Response resp = Jsoup.connect(url).method(Connection.Method.GET).ignoreContentType(true).execute();
                if (is_target(resp)) {
                    UFRGSNovo.PreparedDownload(url, url, curent_id);
                }
            } catch (IOException ex) {
                Logger.getLogger(UFRGSNovo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        curent_id = curent_id + _THREADS;
        return 0;
    }

    public void run(int from_id, int to_id) throws InterruptedException {
        download_incrementing();
    }
    private static final Runnable t1 = new Runnable() {
        @Override
        public void run() {
            try {
                download_incrementing();
            } catch (InterruptedException ex) {
                Logger.getLogger(UFRGSNovo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public static String download_all() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        Thread tread;
        for (int i = 0; i < _THREADS; i++) {
            tread = new Thread(t1);
            threads.add(tread);
            tread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return "";
    }

    public static boolean is_target(Response response) {
        boolean is_target = true;
        System.out.println("status_code : " + response.statusCode() + " ---- Url : " + response.statusMessage());
        if (response.statusCode() == 302) {
            is_target = false;
        }
        return is_target;
    }

    public static void main(String[] args) throws InterruptedException {
        from_id = 1800;
        to_id = 106000;
        download_all();
    }
}
