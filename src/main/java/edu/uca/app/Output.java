package edu.uca.app;

import java.time.LocalDateTime;
import java.util.*;

public class Output {
    // -------------------- Utils --------------------
    public void print(String s){ System.out.print(s); }
    public void println(String s){ System.out.println(s); }

    public Output() {}

    protected void Start() {
        println("=== UCA Course Registration (Baseline) ===");
        println("NOTE: This code is intentionally messy. You'll refactor it.");
    }

    protected void Exit() {
        println("Goodbye!");
    }
}
