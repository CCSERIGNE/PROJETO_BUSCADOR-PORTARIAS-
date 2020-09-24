/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.variaveis.globais;

/**
 *
 * @author Serigne Khassim Mbaye 
 * Estudante Ciênça da computação
 * Email : serignekhassim@gmail.com
 * 19-09-2020 -  IBIRUBA - RS IFRS
 */

public class VarivaisGlobais {
    public static  String DEST ;
    public static  String SRCARCH ;
    public static  boolean ADD_Index;
    public static  boolean UPDATE;
    
    public static String getDestinario(){
        return DEST;
    }
    
    public static String getLocaliz(){
        return SRCARCH;
    }
    
    public static void SetDestinario( String destinario){
        DEST = destinario;
    }
    
    public static void SetLocalizao(String localizacao){
        SRCARCH = localizacao;
    }

    
}
