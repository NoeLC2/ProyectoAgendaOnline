package readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Month;
import java.time.Year;
import java.util.Scanner;

import fileclasses.Config;
import java.io.InputStream;
import fileclasses.Config;

public class ConfigReader {

    public static Config getConfig() {

        Config config = null;
        InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("ConfigIntFiles/config.txt");
        //File file = new File("");

        Scanner sc = new Scanner(is);
        String line1 = sc.nextLine();
        String[] dataArray1 = line1.split(" ");
        Year year = Year.of(Integer.parseInt(dataArray1[0]));
        Month month = Month.of(Integer.parseInt(dataArray1[1]));

        String line2 = sc.nextLine();
        String[] dataArray2 = line2.split(" ");
        String inputLang = dataArray2[0];
        String outputLang = dataArray2[1];
        config = new Config(year, month, inputLang, outputLang);

        return config;
    }

}
