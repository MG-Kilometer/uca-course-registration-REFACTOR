package edu.uca.model;

/*
    Define what a student is. Store student information.
 */

public class Student {
    private final BannerID id;
    private final Name name;
    private final Email email;
    public Student(BannerID id, Name name, Email email) {
        this.id=id; this.name=name; this.email=email;
    }
    public String toString() { return id + " " + name + " <" + email + ">"; }

    /* ----------------
     * GETTERS/SETTERS
     * ----------------
     */
    public BannerID getId() { return id; }
    public Name getName() { return name; }
    public Email getEmail() { return email; }
}