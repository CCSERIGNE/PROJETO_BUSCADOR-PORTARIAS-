package api.collection.portarias;

import api.variaveis.globais.VarivaisGlobais;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfSplitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class SplitePdf {
//  Antigo  Atual Official  UFRGS

    protected static void ReadPdfs() throws IOException {
        System.setProperty("user.dir", VarivaisGlobais.SRCARCH);
        File dir = new File(System.getProperty("user.dir"));
        String childs[] = dir.list();
        for (String SplitnomeArchiv : childs) {
            String nomeTXT[] = SplitnomeArchiv.split(".pdf");
            System.out.println("File : " + nomeTXT[0]);
            String NomeCerto = "";
            NomeCerto = nomeTXT[0].replaceAll("/", "_");
            NomeCerto = NomeCerto.replaceAll("º", "numAcent");
            String DEST_F = VarivaisGlobais.DEST + "" + NomeCerto.toString().trim() + ".pdf";
            File file = new File(VarivaisGlobais.DEST);
            file.getParentFile().mkdirs();
            String SRC_F = VarivaisGlobais.SRCARCH + "" + SplitnomeArchiv.toString().trim();
            DEST_F = DEST_F.replace("%C3%A7", "ç");
            DEST_F = DEST_F.replace("%C3%A3", "ã");
            DEST_F = DEST_F.replace("%C3%87", "Ç");
            DEST_F = DEST_F.replace("%C2%BA", "numAcent");
            new SplitePdf().manipulatePdf(DEST_F, SRC_F);
        }
    }

    protected void manipulatePdf(final String dest, final String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));

        List<PdfDocument> splitDocuments = new PdfSplitter(pdfDoc) {
            int partNumber = 1;

            @Override
            protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
                try {
                    return new PdfWriter(String.format(dest, partNumber++));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }.splitBySize(200000);
        
        //opçao 2  : for (PdfDocument doc : splitDocuments)
        splitDocuments.forEach((doc) -> {
            doc.close();
        });

        pdfDoc.close();
    }
    
    
     public static void main(String[] args) throws IOException {
        ReadPdfs();
    }

}
