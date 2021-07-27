/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import static api.covertion.xml.PegandoValores.LeExpressoesRegularesTxt;
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

    public String IdentPortaria(ArrayList<String> expressoes, String text) throws FileNotFoundException, IOException {
        String[] dat;
        Pattern n = Pattern.compile("[0-9]{1,10}");

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

            Matcher numeros = n.matcher(proxSplit_2[0].toString().trim());

            if (numeros.find()) {
                IdPortaria = numeros.group();
            } else {
                IdPortaria = proxSplit_2[0].toString().trim();
            }
        }
        IdPortaria = PegandoValores.VerificaExpressoes(expressoes, text);

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
        ArrayList<String> expressoes = LeExpressoesRegularesTxt();

        in = new BufferedReader(new FileReader(src));
        FileWriter savefile = new FileWriter(new File(dest));
        String str;
        boolean escreve = false;
        boolean finalizar = false;

        String NameArchiv = nomeArquivo;
        if (NameArchiv.startsWith("http")) {
            NameArchiv = NameArchiv.replace("_DOISpont__baraduplas_", "://");
            NameArchiv = NameArchiv.replace("_DOISpont_", ":");
            NameArchiv = NameArchiv.replace("_baraduplas_", "//");
            NameArchiv = NameArchiv.replace("barra", "/");
            NameArchiv = NameArchiv.replace("interrogacao", "?");
        } else {
            NameArchiv = "https://www1.ufrgs.br/sistemas/sde/gerencia-documentos/index.php/publico/ExibirPDF?documento=" + NameArchiv;
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
            str = str.trim();
            Element portaria = new Element("portaria");
            Element text = new Element("text");
            if (escreve == false) {
                Pattern p = Pattern.compile("(^)PORTARIA");
                Matcher rege = p.matcher(str);

                if (rege.find()) {
                    escreve = true;
                    VarivaisGlobais.QtdPortarias++;
                    GetDAtePortaria(str);
                }
            }

            if (escreve == true) {

                if (IdPortaria.length() <= 0) {
                    IdentPortaria(expressoes, str);
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
                if (rege_poll.lookingAt()) {
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
                } else if (fimt == (proximoLinha + 1)) {
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
        escreve = false;
        XMLOutputter saveXML = new XMLOutputter();
        saveXML.output(Doc, savefile);

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
