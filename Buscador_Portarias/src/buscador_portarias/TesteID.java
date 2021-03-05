/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador_portarias;

import static api.buscador.portarias.ConvertXML_doc.getdocXML;
import api.variaveis.globais.VarivaisGlobais;
import java.io.File;
import java.io.IOException;
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
public class TesteID {

    public static void TesteIdNovo(String idNovo, String urlNovo, String dataPortaria) throws IOException {

        String[] urlSeparada = urlNovo.split("ano=");
        urlSeparada = urlSeparada[1].split("&numero");
        String ano = urlSeparada[0];

        System.out.println(idNovo + " - " + dataPortaria + " - " + urlNovo);

        JSONObject jsonObject = pegaJSON(ano);
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyvalue = jsonObject.get(keyStr);

            if (keyvalue instanceof JSONObject) {
                JSONArray json = (JSONArray) ((JSONObject) keyvalue).get("results");
                if (json.get(0) instanceof JSONObject) {
                    int corretoAnterior = VarivaisGlobais.NovoIdCorreto;
                    for (Object object : json) {
                        String url = ((JSONObject) object).get("url").toString();

                        Object portaria = ((JSONObject) object).get("portaria");
                        String numero = ((JSONObject) portaria).get("numero").toString();

                        Object dataEfetiva = ((JSONObject) portaria).get("dataEfetiva");
                        Object date = ((JSONObject) dataEfetiva).get("date");

                        String dia = ((JSONObject) date).get("day").toString();
                        String mes = ((JSONObject) date).get("month").toString();
                        String anoJson = ((JSONObject) date).get("year").toString();

                        String dataFormatada = FormataData(dia, mes, anoJson);

                        if (idNovo.equals(numero) && urlNovo.equals(url) && dataFormatada.equals(dataPortaria)) {
                            System.out.println(++VarivaisGlobais.NovoIdCorreto);
                            break;
                        }
                    }
                    if (corretoAnterior == VarivaisGlobais.NovoIdCorreto) {
                        System.out.println("Não pegou " + urlNovo);
                    }
                }
            }
        }
    }

    public static JSONObject pegaJSON(String ano) throws IOException {

        String LinkAtual = "https://sippag-web.ifrs.edu.br/api/v1/portaria?ano=" + ano + "&page=0&size=10000";
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

    public static String FormataData(String dia, String mes, String ano) {
        if (dia.startsWith("0")) {
            dia = dia.replace("0", "");
        }

        String mesText = "";
        switch (mes) {
            case "01":
                mesText = "JANEIRO";
                break;
            case "02":
                mesText = "FEVEREIRO";
                break;
            case "03":
                mesText = "MARÇO";
                break;
            case "04":
                mesText = "ABRIL";
                break;
            case "05":
                mesText = "MAIO";
                break;
            case "06":
                mesText = "JUNHO";
                break;
            case "07":
                mesText = "JULHO";
                break;
            case "08":
                mesText = "AGOSTO";
                break;
            case "09":
                mesText = "SETEMBRO";
                break;
            case "10":
                mesText = "OUTUBRO";
                break;
            case "11":
                mesText = "NOVEMBRO";
                break;
            case "12":
                mesText = "DEZEMBRO";
                break;
        }
        String dataFormatada = "DE " + dia + " DE " + mesText + " DE " + ano;
        return dataFormatada;
    }

    public static void BuscaNumero() throws IOException {
        String docsPath = "C:/Users/Igor/Documents/Portarias/NOVO/XML/";

        System.setProperty("user.dir", docsPath);
        File dirf = new File(System.getProperty("user.dir"));

        String childs[] = dirf.list();
        for (String child : childs) {
            VarivaisGlobais.QtdPortarias++;
            String SRC_F = docsPath + "" + child.toString().trim();
            Element Documents = getdocXML(SRC_F);
            List<Element> portarias = Documents.getChildren("portaria");
            String url = Documents.getAttributeValue("site");
            for (Element e : portarias) {
                String ID = e.getAttributeValue("ID");
                String dataPortaria = e.getAttributeValue("data");
                buscador_portarias.TesteID.TesteIdNovo(ID, url, dataPortaria);
            }
        }
        System.out.println(VarivaisGlobais.NovoIdCorreto + "IDs corretos em" + VarivaisGlobais.QtdPortarias + "Portarias");

    }

    public static void main(String[] args) throws IOException {
        BuscaNumero();
    }
}
