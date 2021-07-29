/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import static api.covertion.xml.PegandoValores.IdentPortaria;
import static api.covertion.xml.PegandoValores.GetDAtePortaria;
import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.stream.StreamResult;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author sergi
 */
public class Nova_Convert_XML {

    String date;
    static String nomeArquivo;
    String siteArquivo;
    static int numPort;
    String IdPortaria = "";
    BufferedReader in;
    StreamResult out;
    String DatPort = "";

    public static void ConvertTXT_XML() throws IOException {
        File dir = new File(VarivaisGlobais.SRCARCH);
        String childs[] = dir.list();
        int n = 0;

        for (String SplitnomeArchiv : childs) {

            String nomeTXT[] = SplitnomeArchiv.split(".txt");
            String SRC_F = VarivaisGlobais.SRCARCH + "" + SplitnomeArchiv.trim();
            String DEST_F = VarivaisGlobais.DEST + "" + nomeTXT[0].trim() + ".xml";

            nomeArquivo = nomeTXT[0];
            numPort = n++;
            File file = new File(DEST_F);
            file.getParentFile().mkdirs();

            new Nova_Convert_XML().begin(SRC_F, DEST_F);
        }

        System.out.println("Quantidade portarias " + VarivaisGlobais.QtdPortarias);
        System.out.println("Sem Data " + VarivaisGlobais.QtdSemData);
        System.out.println("Sem numero " + VarivaisGlobais.QtdSemNumero);
    }

    public void begin(String src, String dest) throws FileNotFoundException, IOException {

        in = new BufferedReader(new FileReader(src));
        FileWriter savefile = new FileWriter(new File(dest));
        String str;
        boolean escreve = false;
        boolean finalizar = false;

        String linkPDF = FormataLinkSite(nomeArquivo);

        Element elemDoc = new Element("Document");
        elemDoc.setAttribute("id", "" + numPort + "");
        elemDoc.setAttribute("nome_arquivo", "" + nomeArquivo + "");
        elemDoc.setAttribute("site", linkPDF + "");
        Document Doc = new Document(elemDoc);

        ArrayList<String> linhas = new ArrayList<>();
        int fimt = 0;
        int proximoLinha = 0;
        while ((str = in.readLine()) != null) {
//            System.out.println(str);
            str = str.trim();
            Element portaria = new Element("portaria");
            Element text = new Element("text");

            // Quando encontra a portaria irá começar a escrever
            if (escreve == false) {
                Pattern p = Pattern.compile("(^)PORTARIA");
                Matcher rege = p.matcher(str);

                if (rege.find()) {
                    escreve = true;
                    VarivaisGlobais.QtdPortarias++;
                    DatPort = GetDAtePortaria(str);
                }
            }

            if (escreve == true) {
                if (IdPortaria.length() <= 0) {
                    IdPortaria = IdentPortaria(str);
                }

                Pattern p_2 = Pattern.compile("(^)Diretora Geral|Reitor Substituto|Diretora-Geral|\\(Presidente da CPAD|Reitor pro tempore|Vice-Reitora"
                        + "|Pró-Reitora|Pró-Reitor|Diretor Geral|Diretor-Geral|Vice-Pró-Reitora|Vice-Pró-Reitor|Vice-Superintendente|VICE-REITOR"
                        + "|Vice-Superintendente|Reitor|VICE-REITORA|Diretor|Diretora da Faculdade|(^)End_New_Official");
                Matcher rege_2 = p_2.matcher(str);
                if (rege_2.lookingAt()) {
                    if (IdPortaria.length() > 0) {
                        escreve = false;
                        portaria.setAttribute("ID", IdPortaria);
                        portaria.setAttribute("data", "" + DatPort + "");
                        linhas.add(str);

                        String dados_1 = FormataTexto(linhas);
                        
                        text.setText(dados_1);
                        linhas.removeAll(linhas);
                        portaria.addContent(text);
                        elemDoc.addContent(portaria);

                        DatPort = "";
                        IdPortaria = "";
                    } else {
                        proximoLinha = fimt;
                        finalizar = true;
                    }
                }
            }

            if (finalizar == true && IdPortaria.length() < 1) {
                Pattern poll = Pattern.compile("(^)Port. nº|Portaria");
                Matcher rege_poll = poll.matcher(str);
                if (rege_poll.lookingAt() || fimt == (proximoLinha + 1)) {
                    escreve = false;
                    finalizar = false;
                    proximoLinha = 0;
                    portaria.setAttribute("ID", IdPortaria);
                    portaria.setAttribute("data", "" + DatPort + "");
                    linhas.add(str);

                    String dados_1 = FormataTexto(linhas);

                    text.setText(dados_1);
                    linhas.removeAll(linhas);
                    portaria.addContent(text);
                    elemDoc.addContent(portaria);
                    if (DatPort.equals("")) {
                        VarivaisGlobais.QtdSemData++;
                    }
                    if (IdPortaria.equals("")) {
                        System.out.println(nomeArquivo);
                        VarivaisGlobais.QtdSemNumero++;
                    }
                    DatPort = "";
                    IdPortaria = "";
                }
            }

            if (escreve == true) {
                linhas.add(str);
            }

            fimt++;
        }
        XMLOutputter saveXML = new XMLOutputter();
        saveXML.output(Doc, savefile);
    }

    public static String FormataLinkSite(String arquivo) {
        String nameArchiv = nomeArquivo;
        if (nameArchiv.startsWith("http")) {
            nameArchiv = nameArchiv.replace("_DOISpont__baraduplas_", "://");
            nameArchiv = nameArchiv.replace("_DOISpont_", ":");
            nameArchiv = nameArchiv.replace("_baraduplas_", "//");
            nameArchiv = nameArchiv.replace("barra", "/");
            nameArchiv = nameArchiv.replace("interrogacao", "?");
        } else {
            nameArchiv = "https://www1.ufrgs.br/sistemas/sde/gerencia-documentos/index.php/publico/ExibirPDF?documento=" + nameArchiv;
        }
        return nameArchiv;
    }

    public static String FormataTexto(ArrayList<String> linhas) throws UnsupportedEncodingException {
        byte[] bytes = linhas.toString().getBytes();
        String dados = new String(bytes, "UTF-8");

        dados = dados.replace("[", "");
        dados = dados.replace("]", "");

        while (dados.contains(", ,") || dados.contains(",,")) {
            dados = dados.replaceAll(", ,", ",");
            dados = dados.replaceAll(",,", ",");
        }

        dados = dados.trim();

        return dados;
    }

    public static void main(String[] args) throws IOException {
        Nova_Convert_XML.ConvertTXT_XML();
    }
}
