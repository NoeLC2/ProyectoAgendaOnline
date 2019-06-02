package readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fileclasses.Config;
import fileclasses.International;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public interface InternationalReader {

    String FILE_NAME = "ConfigIntFiles/internacional";

    static International getInternacional(String language) throws UnsupportedEncodingException {

        International international = null;
        InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("ConfigIntFiles/internacional." + language);
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");

        Scanner sc = new Scanner(isr);

        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String line = sc.nextLine();
            String[] dataInfo = line.split(";");
            if (dataInfo[1].indexOf(",") == -1) {
                data.add(dataInfo[1].trim());
            } else {
                data.add(dataInfo[1].split(","));
            }

        }

        international = new International((String) data.get(0), (String[]) data.get(1),
                (String) data.get(2), (String[]) data.get(3), (String[]) data.get(4),
                (String) data.get(5), (String) data.get(6), (String) data.get(7), (String) data.get(8));
        //international = new International(title, weekDays, weekDaysAbbr, months, timeWords, generatedBy, closed, error);

        return international;

    }
}
