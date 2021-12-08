/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.collection.portarias;

import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class DownloadOficialNovo {

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
        NameArchiv = NameArchiv.replace("?", "interrogacao");

        getHttps = getHttps.replace("ç", "%C3%A7");
        getHttps = getHttps.replace("ã", "%C3%A3");
        getHttps = getHttps.replace("Ç", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");
        getHttps = getHttps.replace("Diário", "Di%C3%A1rio");
        getHttps = getHttps.replace("º", "%C2%BA");
        getHttps = getHttps.replace("?ano", "/?ano");
        System.out.println(" receita : " + getHttps);
        System.out.println(" PDF : " + NameArchiv);

        String localpath = VarivaisGlobais.DEST + "" + NameArchiv + ".pdf";

        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }

    public static void printJsonObject(JSONObject jsonObj) {
//        VarivaisGlobais.SetDestinario("C:\\Users\\Igor\\Documents\\PDFs\\Novo\\");
        for (Object key : jsonObj.keySet()) {
            //based on you key types
            String keyStr = (String) key;
            Object keyvalue = jsonObj.get(keyStr);

            //for nested objects iteration if required
            if (keyvalue instanceof JSONObject) {
                System.out.println(((JSONObject) keyvalue).get("results").getClass());
                JSONArray json = (JSONArray) ((JSONObject) keyvalue).get("results");
                if (json.get(0) instanceof JSONObject) {
                    System.out.println(json.get(0));
                    for (Object object : json) {
                        String url = ((JSONObject) object).get("url").toString();
                        JSONObject assinatura = (JSONObject) ((JSONObject) object).get("assinatura");
                        String nome = assinatura.get("interessadoNome").toString();
                        DownloadOficialNovo dow = new DownloadOficialNovo();
                        DownloadOficialNovo.PreparedDownload(url, nome, 0);
                    }
                }
            }
        }
    }

    public static void BaixoPortariasNovo() throws IOException {
        Calendar cal = Calendar.getInstance();
        int anoPortarias = 2021; // Começaram a ser postadas em 2017
        int anoAtual = cal.get(Calendar.YEAR);
        long tempoInicio = System.currentTimeMillis();
        ArrayList<String> ifrsLinks = new ArrayList<>();
        ifrsLinks.add("ifrs");
//        ifrsLinks.add("ifce");      
//        ifrsLinks.add("ifnmg");
        //// ifrsLinks.add("ifsertao-pe"); // NÃO
//        ifrsLinks.add("IFBaiano");
//        ifrsLinks.add("ifal");
        for (String link : ifrsLinks) {

            while (anoPortarias <= anoAtual) {
                VarivaisGlobais.SetDestinario(VarivaisGlobais.getDestinario()+ "\\" + link + "\\" + anoPortarias + "\\");
                String LinkAtual = "https://sippag-web." + link + ".edu.br/api/v1/portaria?ano=" + anoPortarias + "&page=0";
                Response resp = Jsoup.connect(LinkAtual).method(Connection.Method.GET)
                        .header("Host", "sippag-web." + link + ".edu.br")
                        .header("Connection", "keep-alive")
                        .header("Pragma", "no-cache")
                        .header("Cache-Control", "no-cache")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36")
                        .header("DNT", "1")
                        .header("authorization", "null null")
                        .header("content-type", "application/json")
                        .header("Accept", "*/*")
                        .header("Sec-Fetch-Site", "same-origin")
                        .header("Sec-Fetch-Mode", "cors")
                        .header("Sec-Fetch-Dest", "empty")
                        .header("Referer", "https://sippag-web." + link + ".edu.br/portarias/")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7").ignoreContentType(true).execute();
                
          
                try {
                    JSONObject jsonObject = new JSONObject(resp.body());
                    printJsonObject(jsonObject);
                } catch (JSONException err) {
                    System.out.println("Error" + err.toString());
                }
                anoPortarias++;

            }
            
        }
        System.out.println("Tempo Total: "+(System.currentTimeMillis()-tempoInicio));

    }

    public static void main(String[] args) throws IOException {
        BaixoPortariasNovo();
    }
}
