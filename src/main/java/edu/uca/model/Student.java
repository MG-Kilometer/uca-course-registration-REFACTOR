package edu.uca.model;

/*
    Define what a student is. Store student information.
 */

public class Student {
    private final String id, name, email;
    public Student(String id, String name, String email) {
        this.id=id; this.name=name; this.email=email;
    }
    public String toString() { return id + " " + name + " <" + email + ">"; }

    /* ----------------
     * GETTERS/SETTERS
     * ----------------
     */
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}