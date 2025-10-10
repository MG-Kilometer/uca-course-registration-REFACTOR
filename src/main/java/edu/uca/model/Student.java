package edu.uca.model;

public class Student {
    private String id, name, email;
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

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}