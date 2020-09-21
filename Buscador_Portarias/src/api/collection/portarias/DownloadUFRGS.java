/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.collection.portarias;

import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Serigne Khassim Mbaye 
 * Estudante Ciênça da computação
 * Email : serignekhassim@gmail.com
 * 19-09-2020 -  IBIRUBA - RS IFRS
 */

public class DownloadUFRGS {

    /**
     * @param webFile
     * @param NameArchiv
     * @param NumbeArchiv
     * @return 
     */
    public boolean PreparedDownload(String webFile, String NameArchiv, int NumbeArchiv) {
        String[] convertHttps = webFile.split("http:");
        String getHttps;
        
        if (convertHttps[0].isEmpty()) {
            getHttps = "https:" + convertHttps[1];
        } else {
            getHttps = webFile;
        }
        
        getHttps = getHttps.replace("ç", "%C3%A7");getHttps = getHttps.replace("ã", "%C3%A3");getHttps = getHttps.replace("Ç", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");getHttps = getHttps.replace("Diário", "Di%C3%A1rio");getHttps = getHttps.replace("º", "%C2%BA");
        System.out.println(" receita : "+getHttps);
        NameArchiv = NameArchiv.replace( "/" , "_");NameArchiv = NameArchiv.replace("Diário", "Diario");NameArchiv = NameArchiv.replace("º", "_");
        NameArchiv = NameArchiv.replace("ç", "c");NameArchiv = NameArchiv.replace("ã", "a");NameArchiv = NameArchiv.replaceAll("Ç", "c");
        NameArchiv = NameArchiv.replace("---", "_");
        
        String localpath = VarivaisGlobais.DEST+"" + NameArchiv + "_Portaria_UFRGS"  + ".pdf";
        
        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }
    
    public static void BaixarPortariasUFRGS() throws IOException{
        String LinkOficial = "https://www.ufrgs.br/progesp/\\?page_id=5195";
        Document doc = Jsoup.connect(LinkOficial).get();
        String title = doc.title();
        System.out.println("Titel : " + title);
        Elements links = doc.select("a[href]");
        int NumbeArchiv = 0;
        for (Element link : links) {
            //PEGAR OS DOCUMENTOS
            if (link.toString().contains("DocumentosOficiais/Boletins")) {
                String webFile = link.attr("href");
                DownloadUFRGS dow = new DownloadUFRGS();
                dow.PreparedDownload(webFile, link.text(), NumbeArchiv++);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BaixarPortariasUFRGS();
    }
}
