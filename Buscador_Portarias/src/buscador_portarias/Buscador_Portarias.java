/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador_portarias;

import api.buscador.portarias.First_Project_Indexing;
import api.buscador.portarias.First_Project_Search;
import api.collection.portarias.DownloadAntigo;
import api.collection.portarias.DownloadAtual;
import api.collection.portarias.DownloadOficial;
import api.collection.portarias.DownloadUFRGS;
import api.convertion.txt.PDF2TXT;
import api.covertion.xml.XML_Jdom;
import api.variaveis.globais.VarivaisGlobais;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author sergi
 */
public class Buscador_Portarias {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryparser.classic.ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        // TODO code application logic here
        for (int i = 0; i < args.length; i++) {

            if (null != args[i].trim()) {
                switch (args[i].trim()) {
                    case "dest":
                        VarivaisGlobais.SetDestinario(args[i + 1].trim());
                        break;
                    case "localiz":
                        VarivaisGlobais.SetLocalizao(args[i + 1].trim());
                        break;
                    case "collection":
                        switch (args[i + 1]) {
                            case "Antigo":
                                DownloadAntigo.BaixoPortariasAntigo();
                                break;
                            case "Atual":
                                DownloadAtual.BaixarPortariasAtual();
                                break;
                            case "Offical":
                                DownloadOficial.BaixarPortariasOfficial();
                                break;
                            case "UFRGS":
                                DownloadUFRGS.BaixarPortariasUFRGS();
                                break;
                        }
                        break;
                    case "covertTXT":
                        PDF2TXT.Open_Dir();
                        break;
                    case "convertXML":
                        XML_Jdom.Open_DirArchive();
                        break;
                    case "-index":
                        First_Project_Indexing.InicializacaoIndex();
                        break;
                    case "-search":
                        First_Project_Search.BuscarPortaias(args[i + 1].trim());
                        break;
                }
            }
        }
        System.out.println("LOCALIZ--- " + VarivaisGlobais.getLocaliz());
        System.out.println("DEST------ " + VarivaisGlobais.getDestinario());
    }

}
