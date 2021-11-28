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
import api.collection.portarias.DownloadOficialNovo;
import api.collection.portarias.DownloadUFRGS;
import api.convertion.txt.PDF2TXT;
import api.covertion.xml.Nova_Convert_XML;
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
                    
                System.out.println(args[i]);
                
                switch (args[i].trim()) {
                    case "typSit":
                        VarivaisGlobais.setTiposite(args[i + 1].trim());
                        break;
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
                             
                            /*
                            case "NovoUFRGS":
                                UFRGSNovo.from_id = 18000;
                                UFRGSNovo.to_id = 106000;
                                 {
                                    try {
                                        UFRGSNovo.download_all();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Buscador_Portarias.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;
                            */
                            case "Novo":
                                DownloadOficialNovo.BaixoPortariasNovo();
                                break;
                        }
                        break;
                    case "covertTXT":
                        PDF2TXT.Open_Dir(VarivaisGlobais.getTiposite());
                        break;
                    case "convertXML":
                        Nova_Convert_XML.ConvertTXT_XML();
                        break;
                    case "-index":
                        First_Project_Indexing.InicializacaoIndex();
                        break;
                    case "-query":
                        First_Project_Search.BuscarPortaias(args[i + 1].trim());
                        break;
                }
            }
        }
    }

    void main(String query_Edimar_Manica) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
