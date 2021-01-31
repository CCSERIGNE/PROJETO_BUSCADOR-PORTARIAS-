/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador_portarias;

import api.collection.portarias.Downloader;
import api.variaveis.globais.VarivaisGlobais;
import com.sun.glass.events.ViewEvent;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Igor
 */
public class TesteRequisicao {

    public static boolean PreparedDownload(String webFile, String NameArchiv, int NumbeArchiv) {
        String[] convertHttps = webFile.split("http:");
        String getHttps;
        System.out.println(" que: " + convertHttps[0]);
        if (convertHttps[0].isEmpty()) {
            getHttps = "https:" + convertHttps[1];
        } else {
            getHttps = webFile;
        }
       
        NameArchiv = NameArchiv.replace(":", "_DOISpont_");
        NameArchiv = NameArchiv.replace("//", "_baraduplas_");
        NameArchiv = NameArchiv.replace("/", "barra");

        getHttps = getHttps.replace("ç", "%C3%A7");
        getHttps = getHttps.replace("ã", "%C3%A3");
        getHttps = getHttps.replace("Ç", "%C3%87");
        getHttps = getHttps.replace("---", "-%E2%80%93-");
        getHttps = getHttps.replace("Diário", "Di%C3%A1rio");
        getHttps = getHttps.replace("º", "%C2%BA");
        getHttps = getHttps.replace("?ano", "/?ano");
        System.out.println(" receita : " + getHttps);
        

        String localpath = VarivaisGlobais.DEST + "" + NameArchiv + ".pdf";

        NumbeArchiv++;
        Downloader d = new Downloader();
        return d.downloadUrl(getHttps, localpath);
    }

    public static void printJsonObject(JSONObject jsonObj) {
        VarivaisGlobais.SetDestinario("C:\\Users\\Igor\\Documents\\PDFs\\Novo\\");
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
//                        if (object instanceof JSONObject) {
                            String url = ((JSONObject) object).get("url").toString();
                            System.out.println(((JSONObject) object).get("url"));
                            JSONObject assinatura = (JSONObject) ((JSONObject) object).get("assinatura");;
//                            if (assinatura instanceof JSONObject ){
                            String nome = assinatura.get("interessadoNome").toString();
                                System.out.println(nome);
                                TesteRequisicao dow = new TesteRequisicao();
                                
//                                System.out.println(assinatura.get("url"));
//                            }
                            dow.PreparedDownload(url, nome,0);
//                        }
//                        System.out.println();
                    }
                }
//                printJsonObject((JSONObject) keyvalue);
            }
        }
    }

    public static String Request() throws IOException {
        String LinkAtual = "https://sippag-web.ifrs.edu.br/api/v1/portaria?ano=2021&page=0&size=200";
        Response resp = Jsoup.connect(LinkAtual).method(Connection.Method.GET)
                .header("Host", "sippag-web.ifrs.edu.br")
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
                .header("Referer", "https://sippag-web.ifrs.edu.br/portarias/")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7").ignoreContentType(true).execute();
        System.out.println("hum : " + resp.bodyAsBytes());
        try {
            JSONObject jsonObject = new JSONObject(resp.body());
            printJsonObject(jsonObject);
        } catch (JSONException err) {
            System.out.println("Error" + err.toString());
        }
        return "ok";
    }

    public static void main(String[] args) throws IOException {
        System.out.println("ret  : " + Request());
    }
}
