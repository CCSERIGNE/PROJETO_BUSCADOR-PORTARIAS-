package api.convertion.csv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.opencsv.CSVWriter;
import java.io.FileOutputStream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author Iago
 */
public class csv_exporter {

    public static void saveCSV(List<String[]> list, String name) throws IOException {
        String fileName = "C:\\Users\\Igor\\Documents\\Portarias\\Sippag\\CSV\\" + name + ".csv";

        try (FileOutputStream fos = new FileOutputStream(fileName);
                CSVWriter writer = new CSVWriter(
                        new FileWriter(fileName, true),
                        '|',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)) {

            writer.writeAll(list);
        }

    }
}
