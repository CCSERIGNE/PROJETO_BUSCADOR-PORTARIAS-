package api.convertion.csv;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class csv_exporter {

    public static ArrayList<String> organizeCSV(List<String[]> list) throws FileNotFoundException {

        ArrayList<String> listCSV = new ArrayList<>();

        for (String[] item : list) {
            String csv = String.join(", ", item);

            csv = csv.replaceAll("\\*", "");
            csv = csv.replaceAll(". ", "");

            listCSV.add(csv + System.lineSeparator());
        }

        Collections.sort(listCSV);
        return listCSV;
    }

    public static void saveTXT(ArrayList<String> list, String name) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter("C:\\Users\\Igor\\Documents\\Portarias\\Sippag\\CSV\\" + name + ".txt")) {
            for (String item : list) {
                out.write(item);
            }
        }
    }

}
