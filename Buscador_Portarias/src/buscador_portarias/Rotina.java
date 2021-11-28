/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador_portarias;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author Iago
 */
public class Rotina {

    /*
       Essa função é um botão que executa todas as funções possíveis 
       do Touba. Ela ainda NÃO é uma rotina
    
       ATENÇÃO: ANTES DE EXECUTAR VERIFIQUE SE OS LOCAIS ONDE OS ARQUIVOS SERÃO 
       SALVOS SÃO VALIDOS
       Se for alterar os argumentos desta função, lembres-se que as 
       aspas simples "'" que estão nos argumentos também NÃO devem ser colocadas
       aqui e que as "\" contrabarras precisam ser escapados pela "\", ou seja
       \\ no argumento vira ---->>> \\\\ aqui
        
     */
    public static void main(String[] args) throws IOException, ParseException {

        ArrayList<String> sitesOficiais = new ArrayList<String>();
        sitesOficiais.add("Antigo");
        sitesOficiais.add("Atual");
        sitesOficiais.add("Offical");
        sitesOficiais.add("UFRGS");
        sitesOficiais.add("Novo");

        ColetarPDFs(sitesOficiais);
        ConverterTXT(sitesOficiais);
        ConverterXML();
        Indexar();

    }

    private static void ColetarPDFs(ArrayList<String> sitesOficiais) throws IOException, ParseException {
        for (String site : sitesOficiais) {
            String localSave = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\" + site + "\\\\PDF\\\\";
            Buscador_Portarias.main(new String[]{"dest", localSave, "collection", site});
        }
    }

    private static void ConverterTXT(ArrayList<String> sitesOficiais) throws IOException, ParseException {
        for (String site : sitesOficiais) {

            String dest = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Selecionados_csv-Teste\\\\" + site + "\\\\TXT\\\\";
            String localiz = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Selecionados_csv-Teste\\\\" + site + "\\\\PDF\\\\";
            Buscador_Portarias.main(new String[]{"typSit", site, "dest", dest, "localiz", localiz, "covertTXT"});
        }
    }

    private static void ConverterXML() throws IOException, ParseException {
        String dest = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Selecionados_csv-Teste\\\\Antigo\\\\XML\\\\";
        String localiz = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Selecionados_csv-Teste\\\\Antigo\\\\TXT\\\\";
        Buscador_Portarias.main(new String[]{"dest", dest, "localiz", localiz, "convertXML"});
    }
    
    private static void Indexar() throws IOException, ParseException {
            String dest = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Antigo\\\\INDEX\\\\";
            String localiz = "C:\\\\Users\\\\Igor\\\\Documents\\\\Portarias\\\\Antigo\\\\XML\\\\";
            Buscador_Portarias.main(new String[]{"dest", dest, "localiz", localiz, "-index"});
        
    }
}
