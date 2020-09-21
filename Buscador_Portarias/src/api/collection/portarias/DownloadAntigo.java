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
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class DownloadAntigo {

    /**
     * @param webFile
     * @param NameArchiv
     * @param NumbeArchiv
     * @return
     */
    public static boolean PreparedDownload(String webFile, String NameArchiv, int NumbeArchiv) {
        String[] convertHttps = webFile.split("http:");
        String getHttps;
        System.out.println(" que: " + convertHttps[0]);
        if (convertHttps[0].isEmpty()) {
            getHttps = "https:" + convertHttps[1];
        } else {
            getHttps = webFile;
        }

        NameArchiv = getHttps;
        NameArchiv = NameArchiv.replace(":", "_DOISpont_");
        NameArchiv = NameArchiv.replace("//", "_baraduplas_");
        NameArchiv = NameArchiv.replace("/", "barra");

        getHttps = getHttps.replace("Ã§", "%C3%A7");
        getHttps = getHttps.replace("Ã£", "%C3%A3");
        getHttps = getHttps.replace("Ã‡", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");
        getHttps = getHttps.replace("DiÃ¡rio", "Di%C3%A1rio");
        getHttps = getHttps.replace("Âº", "%C2%BA");

        String localpath = VarivaisGlobais.DEST + "" + NameArchiv + ".pdf";
        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }

    public static void BaixoPortariasAntigo() throws IOException {
        // TODO code application logic here
        String LinkAntigo = "https://ibiruba.ifrs.edu.br/site/conteudo.php?cat=50";
        Document doc = Jsoup.connect(LinkAntigo).get();
        String title = doc.title();
        Elements links = doc.select("a[href]");
        System.out.println(" : " + title);
        for (Element link : links) {
            //PEGAR OS DOCUMENTOS

            if (link.text().matches("Boletins de Serviço .*") || link.text().matches("Boletim de Serviço .*")) {
                System.out.println(" links  : " + link.text());
                if (link.attr("abs:href").endsWith("pdf")) {
                    System.out.println("link _ 1: " + link.attr("abs:href")); //abs:href pega a URL completa
                } else {
                    Document doc02 = Jsoup.connect(link.attr("abs:href")).get();
                    Elements links02 = doc02.select("a[href]");
                    for (Element link02 : links02) {
                        if (link02.attr("abs:href").endsWith("pdf")) {
                            String webFile = link02.attr("abs:href");
                            DownloadAntigo dow = new DownloadAntigo();
                            dow.PreparedDownload(webFile, link02.text(), 0);
                            System.out.println("link : " + link02.attr("abs:href")); //abs:href pega a URL completa
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BaixoPortariasAntigo();
    }
}
