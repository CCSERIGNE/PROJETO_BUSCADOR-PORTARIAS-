package api.buscador.portarias;

import static api.buscador.portarias.ConvertXML_doc.getdocXML;
import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.File;
import java.util.List;
import org.apache.lucene.document.Document;
import org.jdom2.Element;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 *
 * "C:\\Users\\sergi\\Documents\\NetBeansProjects\\First_Project_Indexing\\index";
 * //director para salvar index
 * "C:\\Users\\sergi\\Documents\\PDFextraid\\Julho_2020_pdfs\\XML\\" + Pasta +
 * "";// arquivos xml;
 */
public class First_Project_Indexing {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        InicializacaoIndex();
    }

    public static void InicializacaoIndex() throws IOException {
        String indexPath = VarivaisGlobais.DEST;
        String docsPath = VarivaisGlobais.SRCARCH;
        boolean create = false;

        // Exclui tudo no diretório destino
        File folder = new File(indexPath);
        if (folder.isDirectory()) {
            File[] sun = folder.listFiles();
            for (File toDelete : sun) {
                toDelete.delete();
            }
        }

        if (VarivaisGlobais.ADD_Index) {
            create = true;
        } else if (VarivaisGlobais.UPDATE) {
            create = false;
        }

        if (docsPath == null) {
            System.err.println("Usage: ");
            System.exit(1);
        }

        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        System.setProperty("user.dir", docsPath);
        File dirf = new File(System.getProperty("user.dir"));

        Date start = new Date();
        System.out.println("Indexing to directory '" + indexPath + "'...");
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setRAMBufferSizeMB(1024);// Tamanho maximo dos arquivos indexados

        if (create) {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        IndexWriter writer = new IndexWriter(dir, iwc);
        String childs[] = dirf.list();
        for (String child : childs) {
            String SRC_F = VarivaisGlobais.SRCARCH + "" + child.toString().trim();
            Element Documents = getdocXML(SRC_F);
            List<Element> portarias = Documents.getChildren("portaria");
            for (Element e : portarias) {
                String ID = e.getAttributeValue("ID");

                IndexarXML(writer, Documents.getAttributeValue("site"), e.getChildText("text"), Files.getLastModifiedTime(docDir).toMillis(),
                        ID, e.getAttributeValue("data"));
            }
        }
        writer.close();

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
    }

    /**
     * Indexes a single document
     */
    static void IndexarXML(IndexWriter write, String link, String Conteudo, long lastModified, String numP, String data) throws IOException {
        Document doc = new Document();
        Field pathField = new StringField("path", link, Field.Store.YES);
        Field pathField_2 = new StringField("Nume", numP, Field.Store.YES);
        Field pathField_3 = new StringField("date", data, Field.Store.YES);
        doc.add(pathField);
        doc.add(pathField_2);
        doc.add(pathField_3);
        doc.add(new LongPoint("modified", lastModified));

        byte[] conteudoBytes = Conteudo.getBytes(StandardCharsets.UTF_8);
        Conteudo = new String(conteudoBytes, StandardCharsets.UTF_8);

        Field Field_4 = new TextField("contents", Conteudo, Field.Store.YES);
        doc.add(Field_4);

        write.addDocument(doc);

    }
}
