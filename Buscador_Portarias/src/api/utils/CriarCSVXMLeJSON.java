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

public class CriarCSVXMLeJSON {

    private final String ANO = "2021";
    private ArrayList<String> ifInstituicoes;

    CriarCSVXMLeJSON() {
        this.ifInstituicoes = new ArrayList<>();
//        this.ifInstituicoes.add("ifrs"); // FEITO
        this.ifInstituicoes.add("ifce");
//        this.ifInstituicoes.add("ifnmg"); //FEITO
//        this.ifInstituicoes.add("ifsertao-pe"); // NÃO
//        this.ifInstituicoes.add("IFBaiano");
//        this.ifInstituicoes.add("ifal"); // FEITO
    }

    private void getCSVs() throws IOException {
        List<String[]> csvJson = new ArrayList<>();
        List<String[]> csvXml = new ArrayList<>();
        ArrayList<String> listAllCSVsJSON = new ArrayList<>();
        ArrayList<String> listAllCSVsXML = new ArrayList<>();
        ArrayList<String> csv;

        for (String instituto : ifInstituicoes) {

            csvJson = pegaJSONValores(instituto);
            csv = csv_exporter.organizeCSV(csvJson, "json");
            listAllCSVsJSON.addAll(csv);
            csv_exporter.saveTXT(csv, instituto + "-json");

            csvXml = pegaXMLvalores(instituto);
            csv = csv_exporter.organizeCSV(csvXml, "xml");
            listAllCSVsXML.addAll(csv);
            csv_exporter.saveTXT(csv, instituto + "-xml");

        }

        csv_exporter.saveTXT(listAllCSVsJSON, "tudo-json");
        csv_exporter.saveTXT(listAllCSVsXML, "tudo-xml");

    }

    private List<String[]> pegaJSONValores(String instituto) throws IOException {

        List<String[]> list = new ArrayList<>();

        //Se ultrapasssar o json dá erro, tem que sempre mudar para um valor que pegue todos
        for (int page = 0; page <= 2; page++) {

            String link = "https://sippag-web." + instituto + ".edu.br/api/v1/portaria" + "?ano=" + ANO + "&page=" + page + "&size=1999";

            JSONObject jsonObject = pegaJSON(link);
            for (Object key : jsonObject.keySet()) {
                String keyStr = (String) key;
                Object keyvalue = jsonObject.get(keyStr);

                if (keyvalue instanceof JSONObject) {
                    JSONArray json = (JSONArray) ((JSONObject) keyvalue).get("results");
                    if (json.get(0) instanceof JSONObject) {
                        for (Object object : json) {

                            // Curiosidade: Apesar de não apresentar o link
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

        }
        return list;
    }

    private static JSONObject pegaJSON(String link) throws IOException {

        System.out.println("Link: " + link);

        Connection.Response resp = Jsoup.connect(link).method(Connection.Method.GET)
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

    private List<String[]> pegaXMLvalores(String instituto) throws IOException {
        List<String[]> list = new ArrayList<>();

        String directory = "C:\\Users\\Igor\\Documents\\Portarias\\Sippag\\XML\\" + instituto + "\\" + ANO + "\\";

        System.out.println("Diretorio " + directory);
        System.setProperty("user.dir", directory);
        File dirf = new File(System.getProperty("user.dir"));

        String childs[] = dirf.list();
        for (String child : childs) {
            VarivaisGlobais.QtdPortarias++;

            String SRC_F = directory + "" + child.trim();
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
                    text = text.replaceAll(",", "");
                }

                String[] item = {url, ID, dataPortaria, text};
                list.add(item);
            }
        }

        return list;
    }

    public static void main(String[] args) throws IOException {
        // Atenção o JSON e o XML são baixados em uma ordem diferente
        // então no csv selecione todas as linhas e clique em classificar A-Z
        // Você vai ver que a codificação está estranha. Para isso você deve fazer 
        // Igual este video https://www.youtube.com/watch?v=eKCO6ibfT_w
        // (Não encontrei nenhuma maneira de fazer isso diretamente)
        CriarCSVXMLeJSON csvSaver = new CriarCSVXMLeJSON();
        csvSaver.getCSVs();
    }
}
