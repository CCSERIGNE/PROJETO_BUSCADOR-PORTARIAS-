/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.buscador.portarias;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author sergi
 */
public class ConvertXML_doc {
    
    public static Element getdocXML(String src){
        SAXBuilder builder = new SAXBuilder();
        Document doc;
         Element agenda = null ;
        try {
            doc = builder.build(new File(src));
            agenda = doc.getRootElement();
            return agenda;
        } catch (JDOMException ex) {
            Logger.getLogger(ConvertXML_doc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConvertXML_doc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return agenda;
    }
    
}
