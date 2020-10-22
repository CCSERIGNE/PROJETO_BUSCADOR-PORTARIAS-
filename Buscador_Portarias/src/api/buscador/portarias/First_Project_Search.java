package api.buscador.portarias;

import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Serigne Khassim Mbaye Estudante Ciênça da computação Email :
 * serignekhassim@gmail.com 19-09-2020 - IBIRUBA - RS IFRS
 */
public class First_Project_Search {

    private First_Project_Search() {
    }
    /**
     * Simple command-line based search demo. argumentos: -index
     * "C:\Users\sergi\Documents\NetBeansProjects\Lucene_Verifing\index" -query
     * "portaria"
     */
    static JSONArray moo = new JSONArray();

    public static void main(String[] args) throws Exception {
        String usage = "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.exit(0);
        }
        
        String queryStringget = "";
        queryStringget = "edimar";

        for (int i = 0; i < args.length; i++) {
            if ("-query".equals(args[i])) {
                queryStringget = args[i + 1];
            }
        }
        
        BuscarPortaias(queryStringget);
    }

    public static void BuscarPortaias(String queryStringPes) throws IOException, ParseException {

        String index = "index";
        String field = "contents";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        String queryString = queryStringPes;
        int hitsPerPage = 50;

        index = "C://Users//Igor//Documents//PDFextraid//Indexado//";//Caminho dos itens indexados


        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        BufferedReader in = null;
        if (queries != null) {
            in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        } else {
            in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }
        QueryParser parser = new QueryParser(field, analyzer);
        while (true) {
            if (queries == null && queryString == null) { // prompt the user
                System.out.println("Enter query: ");
            }

            String line = queryString != null ? queryString : in.readLine();

            if (line == null || line.length() == -1) {
                break;
            }

            line = line.trim();
            if (line.length() == 0) {
                break;
            }

            Query query = parser.parse(line);
//      System.out.println("Searching for: " + query.toString(field));

            if (repeat > 0) {                           // repeat & time as benchmark
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                    searcher.search(query, 100);
                }
                Date end = new Date();
                System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");;
            }

            doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

            if (queryString != null) {
                break;
            }
        }
        reader.close();

    }

    /**
     * This demonstrates a typical paging search scenario, where the search
     * engine presents pages of size n to the user. The user can then go to the
     * next page if interested in the next hits.
     *
     * When the query is executed for the first time, then only enough results
     * are collected to fill 5 result pages. If the user wants to page beyond
     * this limit, then the query is executed another time and all hits are
     * collected.
     *
     * @param in
     * @param searcher
     * @param query
     * @param hitsPerPage
     * @param raw
     * @param interactive
     * @throws java.io.IOException
     */
    public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
            int hitsPerPage, boolean raw, boolean interactive) throws IOException {

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits.value);
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);

        while (true) {
            if (end > hits.length) {
                System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents collected.");
                System.out.println("Collect more (y/n) ?");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n') {
                    break;
                }

                hits = searcher.search(query, numTotalHits).scoreDocs;
            }

            end = Math.min(hits.length, start + hitsPerPage);

            for (int i = start; i < end; i++) {
                JSONObject Myobjects = new JSONObject();
                if (raw) {  // output raw format
                    continue;
                }

                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("path");
                String num = doc.get("Nume");
                String dat = doc.get("date");
                String dados = doc.get("contents");
                if (path != null) {
                    Myobjects.put("nameDoc", path);
                }
                if (num != null) {
                    Myobjects.put("numPort", num);
                }

                if (dat != null) {
                    Myobjects.put("datePort", dat);
                }

                if (dados != null) {
                    Myobjects.put("conteudo", dados);
                }

                moo.put(Myobjects);
            }

            if (!interactive || end == 0) {
                break;
            }

            if (numTotalHits >= end) {
                boolean quit = false;
                OUTER:
                while (true) {
                    if (start - hitsPerPage >= 0) {
                        System.out.print("(p)revious page, ");
                    }
                    if (start + hitsPerPage < numTotalHits) {
                        System.out.print("(n)ext page, ");
                    }
                    System.out.println("(q)uit or enter number to jump to a page.");
                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0) == 'q') {
                        quit = true;
                        break;
                    }
                    switch (line.charAt(0)) {
                        case 'p':
                            start = Math.max(0, start - hitsPerPage);
                            break OUTER;
                        case 'n':
                            if (start + hitsPerPage < numTotalHits) {
                                start += hitsPerPage;
                            }
                            break OUTER;
                        default:
                            int page = Integer.parseInt(line);
                            if ((page - 1) * hitsPerPage < numTotalHits) {
                                start = (page - 1) * hitsPerPage;
                                break OUTER;
                            } else {
                                System.out.println("No such page");
                            }
                            break;
                    }
                }
                if (quit) {
                    break;
                }
                end = Math.min(numTotalHits, start + hitsPerPage);
            }
        }

        System.out.print(JSONObject.stringToValue(moo.toString()));
    }
}