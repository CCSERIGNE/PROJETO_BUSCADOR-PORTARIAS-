/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.utils;

import static api.buscador.portarias.ConvertXML_doc.getdocXML;
import api.convertion.csv.csv_exporter;
import api.variaveis.globais.VarivaisGlobais;
import buscador_portarias.TesteID;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Igor
 */
public class CriarCSVNovoTextoTipo {

    private static final String URLBASE = "https://sippag-web.ifrs.edu.br/api/v1/portaria";
    private static final String ANO = "2021";
    private static final String DIRETORIO = "C:\\Users\\Igor\\Documents\\Portarias\\Sippag\\XML\\ifrs\\2021\\";

    public static void SalvaJsonLinkNumeroURL() throws IOException {

        List<String[]> list = new ArrayList<>();

        JSONObject jsonObject = pegaJSON(ANO);
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyvalue = jsonObject.get(keyStr);

            if (keyvalue instanceof JSONObject) {
                JSONArray json = (JSONArray) ((JSONObject) keyvalue).get("results");
                if (json.get(0) instanceof JSONObject) {
                    for (Object object : json) {

                        // Curiosidade aleatória: Apesar de não apresentar o link
                        // é possível baixar o PDF colocando o numero e o hash
                        String url = ((JSONObject) object).get("url").toString();
                        if (url.equals("null")) {
                            continue;
                        }

                        Object portaria = ((JSONObject) object).get("portaria");
                        String numero = ((JSONObject) portaria).get("numero").toString();

                        Object dataEfetiva = ((JSONObject) portaria).get("dataEfetiva");
                        Object date = ((JSONObject) dataEfetiva).get("date");

                        String dia = ((JSONObject) date).get("day").toString();
                        String mes = ((JSONObject) date).get("month").toString();
                        String anoJson = ((JSONObject) date).get("year").toString();
                        
                        String tipoPortaria = ((JSONObject) object).get("tipoPortaria").toString();

                        String dataFormatada = TesteID.FormataData(dia, mes, anoJson);

                        String[] item = {url, numero, dataFormatada, tipoPortaria};
                        list.add(item);

                    }
                }
            }
        }
        csv_exporter.saveCSV(list, "json");
    }

    public static JSONObject pegaJSON(String ano) throws IOException {

        String LinkAtual = URLBASE + "?ano=" + ano + "&page=0";
        
        System.out.println("Link: "+ LinkAtual);
        
        Connection.Response resp = Jsoup.connect(LinkAtual).method(Connection.Method.GET)
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
        try {
            JSONObject jsonObject = new JSONObject(resp.body());
            return jsonObject;
        } catch (JSONException err) {
            System.out.println("Error" + err.toString());
            return null;

        }
    }

        public static void SalvaXMLLinkNumeroURL() throws IOException {
        System.out.println("Diretorio " + DIRETORIO);
        System.setProperty("user.dir", DIRETORIO);
        File dirf = new File(System.getProperty("user.dir"));

        List<String[]> list = new ArrayList<>();

        String childs[] = dirf.list();
        for (String child : childs) {
            VarivaisGlobais.QtdPortarias++;
            String SRC_F = DIRETORIO + "" + child.trim();
            Element Documents = getdocXML(SRC_F);
            List<Element> portarias = Documents.getChildren("portaria");
            String url = Documents.getAttributeValue("site");
            for (Element e : portarias) {
                String ID = e.getAttributeValue("ID");
                String dataPortaria = e.getAttributeValue("data");
                
                
                List<Element> textElement = e.getChildren("text");
                
                String text = "";
                for (Element t : textElement) {
                    byte[] textoBytes = t.getText().getBytes();
                    text = new String(textoBytes, StandardCharsets.UTF_8);
                }

                String[] item = {url, ID, dataPortaria, text};
                list.add(item);
            }
        }
        csv_exporter.saveCSV(list, "xml");

    }

    public static void main(String[] args) throws IOException {
        // Atenção o JSON e o XML são baixados em uma ordem diferente
        // então no csv selecione todas as linhas e clique em classificar A-Z
        // Você vai ver que a codificação está estranha. Para isso você deve fazer 
        // Igual este video https://www.youtube.com/watch?v=eKCO6ibfT_w
        // (Não encontrei nenhuma maneira de fazer isso diretamente)
        SalvaXMLLinkNumeroURL();
        SalvaJsonLinkNumeroURL();
    }
}
