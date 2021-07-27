/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Igor
 */
public class PegandoValores {

    static ArrayList<String> expressoes;

    static {
        try {
            expressoes = LeExpressoesRegularesTxt();
        } catch (IOException ex) {
            Logger.getLogger(PegandoValores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> LeExpressoesRegularesTxt() throws IOException {
        String diretorio = System.getProperty("user.dir");
        diretorio = diretorio.replace("Buscador_Portarias", "\\Arquivos\\expressoesRegulares.txt");
        System.out.println("Expressões Regulares --> " + diretorio);

        ArrayList<String> expressoesLista = new ArrayList<>();

        try (BufferedReader buffRead = new BufferedReader(new FileReader(diretorio))) {
            String linha = "";
            while (true) {
                if (linha != null) {
                    linha = buffRead.readLine();
                    expressoesLista.add(linha);
                } else {
                    break;
                }
            }
        }

        return expressoesLista;
    }

    public static String VerificaExpressoes(String text) throws IOException {
        text = text.toUpperCase();

        for (int i = 0; i < expressoes.size() - 1; i++) {
            Pattern pattern = Pattern.compile(expressoes.get(i));
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    public static String IdentPortaria(String text) throws FileNotFoundException, IOException {
        String idPortaria;
        idPortaria = VerificaExpressoes(text);
        return idPortaria;
    }

    public static String GetDAtePortaria(String text) {
        String datPort = "";
        if (text.contains(" ")) {
            String[] dat;
            if (text.contains(",")) {
                dat = text.split(",");
                datPort = dat[1].toString().trim();
            } else if (text.contains("de")) {
                dat = text.split("de");
                int h;
                if (text.contains("DIA")) {
                    dat = text.split("DIA");
                }
                for (h = 1; h < dat.length; h++) {
                    datPort = dat[h].toString().trim();
                }
            } else if (text.contains("DE")) {
                dat = text.split("DE");
                int m = 0;
                if (text.contains("DIA")) {
                    dat = text.split("DIA");
                    for (m = 1; m < dat.length; m++) {
                        datPort = dat[m].toString().trim();
                    }
                } else {
                    String dtpor = dat[m].toString().trim();
                    for (m = 2; m < dat.length; m++) {
                        dtpor = dtpor + " " + dat[m].toString().trim();
                        datPort = dtpor.toString();
                    }
                }
            }
        }
        return datPort;
    }
}
