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

    static ArrayList<String> expressoesNumero;
    static ArrayList<String> expressoesData;

    static {
        try {
            expressoesNumero = LeExpressoesRegularesNumeroTxt();
            expressoesData = LeExpressoesRegularesDataTxt();

        } catch (IOException ex) {
            Logger.getLogger(PegandoValores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> LeExpressoesRegularesNumeroTxt() throws IOException {
        String diretorio = System.getProperty("user.dir");
        diretorio = diretorio.replace("Buscador_Portarias", "\\Arquivos\\expressoesRegularesNumero.txt");
        System.out.println("Expressões Regulares --> " + diretorio);

        ArrayList<String> expressoesNumeroLista = new ArrayList<>();

        try (BufferedReader buffRead = new BufferedReader(new FileReader(diretorio))) {
            String linha = "";
            while (true) {
                if (linha != null) {
                    linha = buffRead.readLine();
                    expressoesNumeroLista.add(linha);
                } else {
                    break;
                }
            }
        }

        return expressoesNumeroLista;
    }

    public static ArrayList<String> LeExpressoesRegularesDataTxt() throws IOException {
        String diretorio = System.getProperty("user.dir");
        diretorio = diretorio.replace("Buscador_Portarias", "\\Arquivos\\expressoesRegularesData.txt");
        System.out.println("Expressões Regulares --> " + diretorio);

        ArrayList<String> expressoesDataLista = new ArrayList<>();

        try (BufferedReader buffRead = new BufferedReader(new FileReader(diretorio))) {
            String linha = "";
            while (true) {
                if (linha != null) {
                    linha = buffRead.readLine();
                    expressoesDataLista.add(linha);
                } else {
                    break;
                }
            }
        }

        return expressoesDataLista;
    }

    public static String VerificaExpressoesNumero(String text) throws IOException {
        text = text.toUpperCase();

        String numeroId = "";
        for (int i = 0; i < expressoesNumero.size() - 1; i++) {
            
            Pattern pattern = Pattern.compile(expressoesNumero.get(i));
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String numeroEncontrado = matcher.group(1);

                if (numeroId.length() == 0 || (numeroEncontrado.length() < numeroId.length())) {
                    numeroId = numeroEncontrado;

                }
            }
        }

        return numeroId;
    }

    // ATENÇÃO: As expressões regulares devem estar em ordem
    public static String VerificaExpressoesData(String text) throws IOException {
        text = text.toUpperCase();

        String data = "";
        for (int i = 0; i < expressoesData.size() - 1; i++) {
            
            Pattern pattern = Pattern.compile(expressoesData.get(i));
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                data = matcher.group(0);
                break;
            }
        }

        return data;
    }

    public static String GetNumeroPortaria(String text) throws FileNotFoundException, IOException {
        String idPortaria;
        idPortaria = VerificaExpressoesNumero(text);
        return idPortaria;
    }

    public static String GetDataPortaria(String text) throws IOException {
        String idPortaria;
        idPortaria = VerificaExpressoesData(text);
        return idPortaria;
    }
}
