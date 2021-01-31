/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.stream.StreamResult;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Serigne Khassim Mbaye 
 * Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 
 * 19-09-2020 - IBIRUBA - RS IFRS
 */
public class XML_Jdom {
    //Antigo  Atual Official  UFRGS 

//    public static final String Pasta = "UFRGS";
//    public static final String DEST = "C:\\Users\\sergi\\Documents\\PDFextraid\\Julho_2020_pdfs\\XML\\" + Pasta + "\\";
//    public static final String SRC = "C:\\Users\\sergi\\Documents\\PDFextraid\\Julho_2020_pdfs\\TXT\\" + Pasta + "\\";
//    public static final String SRCARCH = "C:\\Users\\sergi\\Documents\\PDFextraid\\Julho_2020_pdfs\\TXT\\" + Pasta + "";
    String date;
    static String nomeArquivo;
    String siteArquivo;
    static int numPort;
    String IdPortaria = "";
    BufferedReader in;
    StreamResult out;
    String DatPort = "";

    public static void Open_DirArchive() throws IOException {

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
            new XML_Jdom().begin(SRC_F, DEST_F);
        }
    }

    public void begin(String src, String dest) {
        try {
            in = new BufferedReader(new FileReader(src));
            FileWriter savefile = new FileWriter(new File(dest));
            String str;
            boolean escreve = false;
//            Random random = new Random();
//            numPort = random.nextInt(10);
            String NameArchiv = nomeArquivo;
            NameArchiv = NameArchiv.replace("_DOISpont__baraduplas_", "://");
            NameArchiv = NameArchiv.replace("_DOISpont_", ":");
            NameArchiv = NameArchiv.replace("_baraduplas_", "//");
            NameArchiv = NameArchiv.replace("barra", "/");
            NameArchiv = NameArchiv.replace("interrogacao", "?");
            Element elemDoc = new Element("Document");
            elemDoc.setAttribute("id", "" + numPort + "");
            elemDoc.setAttribute("nome_arquivo", "" + nomeArquivo + "");
            elemDoc.setAttribute("site", NameArchiv + "");
            Document Doc = new Document(elemDoc);

            ArrayList<String> linhas = new ArrayList<>();
            int fimt = 0;

            while ((str = in.readLine()) != null) {
                Element portaria = new Element("portaria");
                Element text = new Element("text");
                if (escreve == false) {
                    Pattern p = Pattern.compile("(^)PORTARIA");
                    Matcher rege = p.matcher(str);
                    if (rege.lookingAt()) {
                        escreve = true;
                        if (str.contains(" ")) {
                            String[] IDPo = str.split(" ");
//                            System.out.println(" intact : " + str);
                            IdPortaria = "";
                            String[] dat;
                            if (str.contains(",")) {
                                dat = str.split(",");
                                if (dat[0].toString().contains(" ")) {
                                    String[] getN = dat[0].toString().split(" ");
                                    IdPortaria = getN[(getN.length - 1)];
                                }
                                DatPort = dat[1].toString().trim();
                            } else if (str.contains("de")) {
                                dat = str.split("de");
                                int h = 1;
                                if (str.contains("DIA")) {
                                    dat = str.split("DIA");
                                }
                                for (h = 1; h < dat.length; h++) {
                                    DatPort = dat[h].toString().trim();
                                }
                            } else if (str.contains("DE")) {
                                dat = str.split("DE");
                                int m = 1;
                                if (str.contains("DIA")) {
                                    dat = str.split("DIA");
                                }
                                for (m = 1; m < dat.length; m++) {
                                    DatPort = dat[m].toString().trim();
                                }
                            }
                            if (str.contains("Nº")) {
                                dat = str.split("Nº");
                                String[] proxSplit = dat[1].toString().split("de");
                                IdPortaria = proxSplit[0].toString().trim();
                            }

                        }
                    }
                }
                if (escreve == true) {
                    Pattern p_2 = Pattern.compile("(^)Diretora Geral|Reitor Substituto|Diretora-Geral|\\(Presidente da CPAD|Reitor pro tempore|Vice-Reitora"
                            + "|Pró-Reitora|Pró-Reitor|Diretor Geral|Diretor-Geral|Vice-Pró-Reitora|Vice-Pró-Reitor|Vice-Superintendente|VICE-REITOR"
                            + "|Vice-Superintendente|Reitor|VICE-REITORA|Diretor|Diretora da Faculdade");
                    Matcher rege_2 = p_2.matcher(str);
                    if (rege_2.lookingAt()) {
                        portaria.setAttribute("ID", IdPortaria);
                        portaria.setAttribute("data", "" + DatPort + "");
                        escreve = false;
                        linhas.add(str);
                        String dados = linhas.toString().replace("[", "");
                        String dados_1 = dados.replace("]", "");
                        text.setText(dados_1);
                        linhas.removeAll(linhas);
                        portaria.addContent(text);
                        elemDoc.addContent(portaria);
                    }
                }

                if (escreve) {
                    linhas.add(str);
                }
            }
            XMLOutputter saveXML = new XMLOutputter();
            try {
                saveXML.output(Doc, savefile);
            } catch (IOException e) {
                Logger.getLogger(XML_Jdom.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (IOException e) {
            Logger.getLogger(XML_Jdom.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) throws IOException {
        XML_Jdom.Open_DirArchive();
    }
}
