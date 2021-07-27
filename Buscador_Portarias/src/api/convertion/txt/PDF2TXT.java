/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.convertion.txt;

import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import static sun.management.Agent.error;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class PDF2TXT {

    public static void Open_Dir(String tipoSite) throws IOException {
        System.setProperty("user.dir", VarivaisGlobais.SRCARCH);
        File dir = new File(System.getProperty("user.dir"));

        String childs[] = dir.list();
        for (String SplitnomeArchiv : childs) {
            String nomeTXT = SplitnomeArchiv.replaceFirst(".pdf", "");
            String SRC_F = VarivaisGlobais.SRCARCH + "" + SplitnomeArchiv.toString().trim();
            String DEST_F = VarivaisGlobais.DEST + "" + nomeTXT.toString().trim() + ".txt";
            File file = new File(DEST_F);
            file.getParentFile().mkdirs();

            LerArquivo(SRC_F, DEST_F, tipoSite);
        }
    }

    public static void LerArquivo(String src, String dest, String tipoSite) {
        try {
            String pdfFile = src;
            System.out.println("des : " + dest);
            File filePDF = new File(pdfFile);
            FileInputStream fileInputStream = new FileInputStream(filePDF);
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(dest));
            PDDocument pdfDocument = null;

            try {
                PDFParser parser = new PDFParser(fileInputStream);
                parser.parse();
                pdfDocument = parser.getPDDocument();
                PDFTextStripper stripper = new PDFTextStripper();

                String textoPDF = stripper.getText(pdfDocument);
                String textoPadronizado = PreProcessadores.PadronizarTxt(textoPDF);

                switch (tipoSite) {
                    case "novo":
                        buffWrite.append(textoPadronizado.concat("End_New_Official"));
                        break;
                    default:
                        buffWrite.append(textoPadronizado);
                        break;
                }

                buffWrite.close();
            } finally {
                if (pdfDocument != null) {
                    try {
                        pdfDocument.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            error(e.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        VarivaisGlobais.setTiposite("novo");
        Open_Dir(VarivaisGlobais.getTiposite());
    }
}
