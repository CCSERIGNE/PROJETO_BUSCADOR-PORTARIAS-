/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.collection.portarias;

import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class DownloadOficial {

    /**
     * @param webFile
     * @param NameArchiv
     * @param NumbeArchiv
     * @return
     */
    public boolean PreparedDownload(String webFile, String NameArchiv, int NumbeArchiv) {
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

        getHttps = getHttps.replace("ç", "%C3%A7");
        getHttps = getHttps.replace("ã", "%C3%A3");
        getHttps = getHttps.replace("Ç", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");
        getHttps = getHttps.replace("Diário", "Di%C3%A1rio");
        getHttps = getHttps.replace("º", "%C2%BA");
        System.out.println(" receita : " + getHttps);

        String localpath = VarivaisGlobais.DEST + "" + NameArchiv + ".pdf";

        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }

    public static void BaixarPortariasOfficial() throws IOException {
        ArrayList<String> GetDado_1 = new ArrayList<>();
        String LinkOficial = "https://ifrs.edu.br/";
        Document doc = Jsoup.connect(LinkOficial).get();
        String title = doc.title();
        System.out.println("Titel : " + title);
        Elements links = doc.select("a[href]");
        int NumbeArchiv = 0;
        for (Element link : links) {
            //PEGAR OS DOCUMENTOS
            String l = "Documentos";
            if (!link.text().equals(l)) {
//                NAO FEZ NADA AQUI  OUTRO HISTORIA
            } else {
                Document doc_segundary = Jsoup.connect(link.attr("href")).get();
                Elements links_segundary = doc_segundary.select("a[href]");
                //BUSCANDO SUBLINKS
                for (Element link_segun : links_segundary) {

                    if (link_segun.attr("href").contains(link.attr("href")) && (!GetDado_1.contains(link_segun.attr("href")))) {
                        GetDado_1.add(link_segun.attr("href"));
                        //VERIFICANDO STADO DE LOCALIZAÇÃO DOCUMENTO
                        String GetDoc = link_segun.text();
                        if ((GetDoc.equals("Boletim de Serviço")) || (GetDoc.equals("Boletim de Pessoal Diário")) || (GetDoc.equals("Boletim de Pessoal Mensal"))) {
                            //VERIFICA SE SAO EXTENÇÃO PDF
                            if (!link_segun.attr("href").contains("pdf")) {
                                System.out.println(" res : " + link_segun.attr("href"));
                                System.out.println(" Link : " + link_segun.text());

                                Document Link_Tree = Jsoup.connect(link_segun.attr("href")).get();
                                for (Element table : Link_Tree.getElementsByClass("table")) {
                                    for (Element row : table.select("tr")) {
                                        for (Element tds : row.select("td")) {
                                            Elements linksT = tds.select("a[href]");
                                            for (Element getlink : linksT) {
                                                if ((!getlink.text().equals("Gabinete")) && (!getlink.text().equals("DGP")) && (!getlink.text().equals("Boletim de Serviço"))
                                                        && (!getlink.text().equals("Boletim de Pessoal Diário")) && (!getlink.text().equals("Boletim de Pessoal Mensal"))) {
                                                    if ((!GetDado_1.contains(Link_Tree.attr("href")))) {

                                                        Document Link_for = Jsoup.connect(getlink.attr("href")).get();
                                                        for (Element table2 : Link_for.select("table")) {
                                                            for (Element row2 : table2.select("tr")) {
                                                                for (Element tds2 : row2.select("td")) {
                                                                    Elements linksT2 = tds2.select("a[href]");
                                                                    for (Element getlink2 : linksT2) {
                                                                        String webFile = getlink2.attr("href");
                                                                        if ((!webFile.contains("dgp")) && (webFile.contains("pdf"))) {
                                                                            DownloadOficial dow = new DownloadOficial();
                                                                            dow.PreparedDownload(webFile, getlink2.text(), NumbeArchiv++);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (link_segun.attr("href").contains("pdf")) {
                            //VAI BAIXAR ARQUIVOS
                            System.out.println("\n documet : " + link_segun.attr("href"));
                            String webFile = link_segun.attr("href");
                            DownloadOficial dow = new DownloadOficial();
                            dow.PreparedDownload(webFile, link_segun.text(), NumbeArchiv++);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BaixarPortariasOfficial();
    }
}
