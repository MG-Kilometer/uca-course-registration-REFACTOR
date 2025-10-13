package edu.uca.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIntegrity {

    public FileIntegrity(String studentsCSV, String coursesCSV, String enrollmentsCSV){

        checkAndCreateCSV(studentsCSV);
        checkAndCreateCSV(coursesCSV);
        checkAndCreateCSV(enrollmentsCSV);

    }

    private void checkAndCreateCSV(String filePath){

        File file = new File(filePath);

        //check if file exists
        if (!file.exists()) {

            //attempt to create a new csv file
            try {
                if (file.createNewFile()) {

                    //optional: write header row if needed
                    FileWriter writer = new FileWriter(file);
                    writer.write(""); //empty file
                    writer.close();

                }
            } catch (IOException ignored) {
                //just doesn't make a fuss if you cannot create the file
            }

        }

    }


}
