/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.convertion.txt;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Iago Dalmolin
 */
public class PreProcessadores {

    public static String PadronizarTxt(String textoTxt) throws UnsupportedEncodingException {
        String textoUTF8 = ConverterUTF8(textoTxt);
        String textoPadronizado = RetirarCaracteresDesnecessarios(textoUTF8);
        return textoPadronizado;
    }

    public static String ConverterUTF8(String texto) throws UnsupportedEncodingException {
        byte[] bytes = texto.getBytes();

        String textoConvertido = new String(bytes, "UTF-8");
        return textoConvertido;
    }

    public static String RetirarCaracteresDesnecessarios(String texto) {
        texto = texto.trim();

        while (texto.contains("  ")) {
            texto = texto.replaceAll("  ", " ");
        }

        texto = texto.replaceAll("°", "º");
        texto = texto.replaceAll("N º", "Nº");
        texto = texto.replaceAll("n º", "nº");

        return texto;
    }
}
