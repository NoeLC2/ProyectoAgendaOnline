package readers;

import fileclasses.Petition;
import output.OutputLogIncidents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public interface PetitionReader {

    static List<Petition> getPetitions(String petitionFile) throws UnsupportedEncodingException, FileNotFoundException {
        //Por comodidad usamos la misma estructura que el lector 
        //de coches que hicimos de ejemplo

        List<Petition> petitions = Collections.emptyList();
        InputStream is;
        InputStreamReader isr;
        //File file = new File( "Petitions/" + petitionFile + ".txt");
        if (petitionFile.equals("custom.txt")) {
            is = new FileInputStream("custom.txt");
            isr = new InputStreamReader(is, "UTF-8");
        } else {
            is = ConfigReader.class.getClassLoader()
                    .getResourceAsStream("Petitions/" + petitionFile + ".txt");
            isr = new InputStreamReader(is, "UTF-8");
        }
        Scanner sc = new Scanner(isr);
        petitions = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.substring(0, 1).equals("#")) {
                Petition petition = createPetition(line);
                if (petition != null) {
                    petitions.add(petition);
                }
            }
        }

        return petitions;
    }

    static Petition createPetition(String line) {

        final byte ACTIVITY = 0, ROOM = 1, STARTDATE = 2, ENDDATE = 3, WEEKDAYS = 4, SCHEDULE = 5;

        String[] dataArray = line.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String activity = dataArray[ACTIVITY];
        activity = activity.substring(0, 1) + activity.substring(1).replaceAll("((?<=\\p{Ll})\\p{Lu})|((?!\\A)\\p{Lu}(?>\\p{Ll}))", " $0").toLowerCase();
        String room = dataArray[ROOM];

        //Throw exception in case the date format is incorrect
        LocalDate startDate = LocalDate.parse(dataArray[STARTDATE], formatter);
        LocalDate endDate = LocalDate.parse(dataArray[ENDDATE], formatter);
        String weekDays = dataArray[WEEKDAYS];
        String schedule = dataArray[SCHEDULE];
        String[] scheduleArray = schedule.split("_");

        Petition petition = new Petition(activity, room, startDate, endDate, weekDays, scheduleArray);
        for (int i = 0; i < scheduleArray.length; i++) {
            if (Integer.parseInt(scheduleArray[i].substring(0, 2)) > Integer.parseInt(scheduleArray[i].substring(3, 5))) {
                //OutputLogIncidents.writeConflict("wrongScheduleFormat", petition);
                return null;
            }
        }
        if (scheduleArray.length > 5) {
            //OutputLogIncidents.writeConflict("TooManyTimePeriods", petition);
            return null;
        } else if (endDate.isBefore(startDate)) {
            //OutputLogIncidents.writeConflict("conflictingDates", petition);
            return null;
        } else {
            return petition;
        }
    }
}
