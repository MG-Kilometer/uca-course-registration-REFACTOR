package edu.uca.util;

import edu.uca.service.Audit;

import java.util.regex.Pattern;

public class Validate {
    private static Boolean result;
    public Audit audit = new Audit();
    private static final int CHAR_LIMIT = 50;

    // constructor that actually validates the data according to pattern supplied and generic rules
    public void Validate_String(String pattern, String data) {
        try {
            if (pattern == null || pattern.isEmpty()) {
                throw new IllegalArgumentException("pattern cannot be null or empty");
            }
            if (data == null || data.isEmpty()) {
                throw new IllegalArgumentException("data cannot be null or empty");
            }
            if (data.length() > CHAR_LIMIT) {
                throw new IllegalArgumentException("data cannot be longer than " + CHAR_LIMIT + " characters");
            }
        } catch (Exception e) {
            audit.add("Error parsing pattern " + pattern + ": " + e.getMessage());
            return;
        }
        result = Pattern.matches(pattern, data);
    }

    public Boolean getResult() {return result;}
}
