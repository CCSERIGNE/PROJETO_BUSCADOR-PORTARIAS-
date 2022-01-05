package api.convertion.csv;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class csv_exporter {

    public static ArrayList<String> organizeCSV(List<String[]> list, String type) throws FileNotFoundException {

        ArrayList<String> listCSV = new ArrayList<>();

        for (String[] item : list) {
            // String csv = String.join(",, ", item); // Fica só uma virgula no txt

            String itemCSV = "";
            for (int i = 0; i < item.length; i++) {
                String partCSV = item[i];
                if (i == item.length - 1 && type.equals("json")) {
                    partCSV = partCSV.replaceAll("\\.", "");
                    partCSV = partCSV.replaceAll("\\*", "");
                    partCSV = partCSV.trim();
                }
                if (i != item.length - 1) {
                    partCSV += ", ";
                }
                itemCSV += partCSV;
            }

            listCSV.add(itemCSV + System.lineSeparator());
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
