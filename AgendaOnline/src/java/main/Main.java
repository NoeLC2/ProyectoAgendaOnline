package main;
import ProcessPetitions.ProcessPetitionsMonth;
import ProcessPetitions.SeparatePetitionsByRoom;
import fileclasses.Config;
import fileclasses.International;
import fileclasses.Petition;
import fileclasses.ProcessedPetition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import output.OutputHTML;
import output.OutputLogIncidents;
import readers.ConfigReader;
import readers.InternationalReader;
import readers.PetitionReader;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    public static String main(String monthYear, String petitions, String outputLang, String bool, String startHour, String endHour) throws UnsupportedEncodingException, FileNotFoundException {
        //long pre = LocalTime.now().toNanoOfDay();
        //Config config = ConfigReader.getConfig();
        String[] arrayMonthYear = monthYear.split("-");
        boolean realBool = (bool == null) ? false : true;
        Config config = new Config(Year.of(Integer.parseInt(arrayMonthYear[0])), Month.of(Integer.parseInt(arrayMonthYear[1])), "ESP", outputLang);

        //Petitions filename, Config object, start and end time for the tables, boolean allowClosedCollision
        String result = execute(petitions, config, Integer.parseInt(startHour), Integer.parseInt(endHour), realBool);
        return result;
        //long post = LocalTime.now().toNanoOfDay();
        //System.out.println("Execution time: " + (post-pre)/1000000 + " ms");
        
    }


    public static String execute(String filename, Config config, int startTime, int endTime, boolean allowClosedCollision) throws UnsupportedEncodingException, FileNotFoundException{
        //We we'll create every object that we'll need
        //OutputLogIncidents.clearLog();
        List<Petition> petitions = PetitionReader.getPetitions(filename);

        International internationalIn = InternationalReader.getInternacional(config.getInputLang());
        International internationalOut = InternationalReader.getInternacional(config.getOutputLang());
        List<ProcessedPetition> processedPetitions = new ArrayList<>();

        //We'll add all the petitions into the ArrayList
        //I could have added them all in getProcessedPetitions, but I wanted to be able to print a specific
        // petition here
        for (Petition petition: petitions){
            if(ProcessPetitionsMonth.getProcessedPetitions(config, petition, internationalIn) != null) {
                processedPetitions.add(ProcessPetitionsMonth.getProcessedPetitions(config, petition, internationalIn));
            }
        }

        //We get a set of different room names
        Set<String> roomsAsSet = new HashSet<String>();
        for (ProcessedPetition p : processedPetitions) {
            roomsAsSet.add(p.getRoom());
        }
        //Now we print an HTML file for each room name
        StringBuilder sb = new StringBuilder();
        roomsAsSet.forEach((e) -> { sb.append(OutputHTML.generateHTML((e), internationalOut, internationalIn,
                SeparatePetitionsByRoom.getPetitions(processedPetitions, (e)), config, startTime, endTime, allowClosedCollision)); });
        //OutputLogIncidents.writeSuccessfulPetitions(petitions, processedPetitions);
        return sb.toString();
    }
    public static String createFile(String monthYear, String request, String outputLang, String languageIn, String bool, String startHour, String endHour) throws IOException{
        /*Path relativePath = Paths.get("cust.txt");
        Path absolutePath = relativePath.toAbsolutePath();
        File file = absolutePath.toFile();*/
        //File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        //FileOutputStream os = new FileOutputStream(new File("Petitions/custom.txt").getCanonicalFile());
        FileWriter fstream = new FileWriter("custom.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(request);
        out.close();
        
        String[] arrayMonthYear = monthYear.split("-");
        boolean realBool = (bool == null) ? false : true;
        Config config = new Config(Year.of(Integer.parseInt(arrayMonthYear[0])), Month.of(Integer.parseInt(arrayMonthYear[1])), languageIn, outputLang);
        String result = execute("custom.txt", config, Integer.parseInt(startHour), Integer.parseInt(endHour), realBool);
        return result;
    }
}
