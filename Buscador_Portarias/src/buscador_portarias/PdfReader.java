/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador_portarias;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author Igor
 */
public class PdfReader {

    public static void main(String args[]) throws IOException {
        //Loading an existing document
        File file = new File("C:\\Users\\Igor\\Documents\\Portarias\\NovoUFRGS\\PDF\\https_DOISpont__baraduplas_www1.ufrgs.brbarrasistemasbarrasdebarragerencia-documentosbarraindex.phpbarrapublicobarraExibirPDFinterrogacaodocumento=18169.pdf");
        PDDocument document = PDDocument.load(file);
        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();
        //Retrieving text from PDF document
        String text = pdfStripper.getText(document);
        System.out.println(text);
        //Closing the document
        document.close();
        
    }
}
