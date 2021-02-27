/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

        System.setProperty("user.dir", VarivaisGlobais.SRCARCH);
        File dir = new File(System.getProperty("user.dir"));

        String childs[] = dir.list();
        int n = 0;

        for (String SplitnomeArchiv : childs) {

            String nomeTXT[] = SplitnomeArchiv.split(".txt");
            String SRC_F = VarivaisGlobais.SRCARCH + "" + SplitnomeArchiv.toString().trim();
            String DEST_F = VarivaisGlobais.DEST + "" + nomeTXT[0].toString().trim() + ".xml";

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

    public String IdentPortaria(String text) {
        String[] dat;

        Pattern p1 = Pattern.compile("(^)Nº [0-9]+/[0-9]{4}");
        Pattern p2 = Pattern.compile("(^)Port. nº [0-9]+/[0-9]{4}");
        Pattern p3 = Pattern.compile("(^)Portaria [0-9]+/[0-9]{4}");
        Pattern p4 = Pattern.compile("(^)PORTARIA N ");
        Matcher rege1 = p1.matcher(text);
        Matcher rege2 = p2.matcher(text);
        Matcher rege3 = p3.matcher(text);
        Matcher rege4 = p4.matcher(text);

        if (text.contains("Nº")) {
            dat = text.split("Nº");
            String[] proxSplit = dat[1].toString().split("de");
            String VChek = proxSplit[0].toString().replaceAll("PORTARIA ", "");
            String[] proxSplit_2 = VChek.split(",");
            if (proxSplit_2.length < 2) {
                proxSplit_2 = VChek.split("DO");
                if (proxSplit_2.length < 2) {
                    proxSplit_2 = VChek.split("DE");
                    if (proxSplit_2.length < 2) {
                    }
                }
            }
            IdPortaria = proxSplit_2[0].toString().trim();
        }
        if (rege1.find()) {
            String[] separaNumero = rege1.group().split("Nº ");
            IdPortaria = separaNumero[1].toString();
        }
        if (rege2.find()) {
            String[] separaNumero = rege2.group().split("Port. nº ");
            IdPortaria = separaNumero[1].toString();
        }
        if (rege3.find()) {
            String[] separaNumero = rege3.group().split("Portaria ");
            IdPortaria = separaNumero[1].toString();
        }
        if (rege4.find()) {
            String[] separaNumero = text.toString().toLowerCase().replaceAll("[a-z]", "").split("   ");
            IdPortaria = separaNumero[0].toString();
        }

        return IdPortaria;
    }

    public String GetDAtePortaria(String text) {
        if (text.contains(" ")) {
            String[] dat;
            if (text.contains(",")) {
                dat = text.split(",");
                DatPort = dat[1].toString().trim();
            } else if (text.contains("de")) {
                dat = text.split("de");
                int h;
                if (text.contains("DIA")) {
                    dat = text.split("DIA");
                }
                for (h = 1; h < dat.length; h++) {
                    DatPort = dat[h].toString().trim();
                }
            } else if (text.contains("DE")) {
                dat = text.split("DE");
                int m = 0;
                if (text.contains("DIA")) {
                    dat = text.split("DIA");
                    for (m = 1; m < dat.length; m++) {
                        DatPort = dat[m].toString().trim();
                    }
                } else {
                    String dtpor = dat[m].toString().trim();
                    for (m = 2; m < dat.length; m++) {
                        dtpor = dtpor + " " + dat[m].toString().trim();
                        DatPort = dtpor.toString();
                    }
                }
            }
        }
        return DatPort;
    }

    public void begin(String src, String dest) throws FileNotFoundException, IOException {

        in = new BufferedReader(new FileReader(src));
        FileWriter savefile = new FileWriter(new File(dest));
        String str;
        boolean escreve = false;
        boolean finalizar = false;

        String NameArchiv = nomeArquivo;
        if (NameArchiv.startsWith("http")) {
            NameArchiv = NameArchiv = NameArchiv.replace("_DOISpont__baraduplas_", "://");
            NameArchiv = NameArchiv.replace("_DOISpont_", ":");
            NameArchiv = NameArchiv.replace("_baraduplas_", "//");
            NameArchiv = NameArchiv.replace("barra", "/");
            NameArchiv = NameArchiv.replace("interrogacao", "?");
        }
        else {
            NameArchiv = "https://www1.ufrgs.br/sistemas/sde/gerencia-documentos/index.php/publico/ExibirPDF?documento="+NameArchiv;
        }

        Element elemDoc = new Element("Document");
        elemDoc.setAttribute("id", "" + numPort + "");
        elemDoc.setAttribute("nome_arquivo", "" + nomeArquivo + "");
        elemDoc.setAttribute("site", NameArchiv + "");
        Document Doc = new Document(elemDoc);

        ArrayList<String> linhas = new ArrayList<>();
        int fimt = 0;
        int proximoLinha = 0;
        while ((str = in.readLine()) != null) {
            Element portaria = new Element("portaria");
            Element text = new Element("text");
            if (escreve == false) {
                Pattern p = Pattern.compile("(^)PORTARIA");
                Matcher rege = p.matcher(str);

                if (rege.find()) {
                    escreve = true;
                    VarivaisGlobais.QtdPortarias++;
                    GetDAtePortaria(str);
                    IdentPortaria(str);

                }
            }

            if (escreve == true) {
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
                        String dados = linhas.toString().replace("[", "");
                        String dados_1 = dados.replace("]", "");
                        text.setText(dados_1);
                        linhas.removeAll(linhas);
                        portaria.addContent(text);
                        elemDoc.addContent(portaria);
//                        if (DatPort.equals("")) {
//                            VarivaisGlobais.QtdSemData++;
//                        }
//                        if (IdPortaria.equals("")) {
//                            System.out.println("aaaaaaa");
//                            VarivaisGlobais.QtdSemNumero++;
//                        }
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
                IdentPortaria(str);
                if (rege_poll.lookingAt()) {
                    escreve = false;
                    finalizar = false;
                    proximoLinha = 0;
                    portaria.setAttribute("ID", IdPortaria);
                    portaria.setAttribute("data", "" + DatPort + "");
                    linhas.add(str);
                    String dados = linhas.toString().replace("[", "");
                    String dados_1 = dados.replace("]", "");
                    text.setText(dados_1);
                    linhas.removeAll(linhas);
                    portaria.addContent(text);
                    elemDoc.addContent(portaria);
                    if (DatPort.equals("")) {
                        VarivaisGlobais.QtdSemData++;
                    }
                    if (IdPortaria.equals("")) {
                        VarivaisGlobais.QtdSemNumero++;
                    }
                    DatPort = "";
                    IdPortaria = "";
                } else if (fimt == (proximoLinha + 1)) {
                    escreve = false;
                    finalizar = false;
                    proximoLinha = 0;
                    portaria.setAttribute("ID", IdPortaria);
                    portaria.setAttribute("data", "" + DatPort + "");
                    linhas.add(str);
                    String dados = linhas.toString().replace("[", "");
                    String dados_1 = dados.replace("]", "");
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
        escreve = false;
        XMLOutputter saveXML = new XMLOutputter();
        saveXML.output(Doc, savefile);

    }

    public static void main(String[] args) throws IOException {
        Nova_Convert_XML.ConvertTXT_XML();
    }
}
