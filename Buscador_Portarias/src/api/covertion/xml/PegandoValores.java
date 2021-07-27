/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.covertion.xml;

import api.variaveis.globais.VarivaisGlobais;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Igor
 */
public class PegandoValores {

    public static String diretorio = "C:\\Users\\Igor\\Documents\\NetBeansProjects\\PROJETO_BUSCADOR-PORTARIAS-\\Arquivos\\expressoesRegulares.txt";

    public static ArrayList<String> LeExpressoesRegularesTxt() throws IOException {

        ArrayList<String> expressoes;
        try (BufferedReader buffRead = new BufferedReader(new FileReader(diretorio))) {
            String linha = "";
            expressoes = new ArrayList<>();
            while (true) {
                if (linha != null) {
                    linha = buffRead.readLine();
                    expressoes.add(linha);
                } else {
                    break;
                }
            }
        }

        return expressoes;
    }

    public static String VerificaExpressoes(ArrayList<String> expressoes, String text) throws IOException {
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
}
