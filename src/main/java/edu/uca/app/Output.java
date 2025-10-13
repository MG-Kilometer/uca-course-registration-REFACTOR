package edu.uca.app;

/*
    Parent class of all classes that output to CLI
 */

public class Output {
    // -------------------- Utils --------------------
    public void print(String s){ System.out.print(s); }
    public void println(String s){ System.out.println(s); }

    public Output() {}

    protected void Start() {
        println("=== UCA Course Registration (Refactored) ===");
        println("NOTE: This code has been refactored.");
    }

    protected void Exit() {
        println("Goodbye!");
    }
}
