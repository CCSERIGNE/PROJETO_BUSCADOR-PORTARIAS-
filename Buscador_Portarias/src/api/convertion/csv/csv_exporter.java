package api.convertion.csv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Iago
 */
public class csv_exporter {

    public static void saveCSV(List<String[]> list, String name) throws IOException {

        try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Igor\\Documents\\Portarias\\Sippag\\CSV\\" + name + ".csv"))) {
            writer.writeAll(list);
        }

    }
}
