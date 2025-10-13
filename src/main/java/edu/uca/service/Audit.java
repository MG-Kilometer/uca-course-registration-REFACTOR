package edu.uca.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    Create a log for all messages about the program's progress
 */

public class Audit {
    static List<String> auditLog = new ArrayList<>();

    public void add(String ev){ auditLog.add(LocalDateTime.now() + " | " + ev); }

}
