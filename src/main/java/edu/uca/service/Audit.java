package edu.uca.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    Create a log for all messages about the program's progress
 */

public class Audit {
    private static final List<String> auditLog = new ArrayList<>();

    public void add(String ev){
        auditLog.add(LocalDateTime.now() + " | " + ev);
        write_logfile();
    }

    public void write_logfile() {
        try (FileWriter fw = new FileWriter("log.txt");) {
            fw.write(auditLog.toString());
        } catch(IOException e) {
            System.out.println("Error writing log file");
        }
    }
}
