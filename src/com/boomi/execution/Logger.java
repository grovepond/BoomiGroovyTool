package com.boomi.execution;

public class Logger {
    public void info(String text) { System.out.println("INFO: " + text); }
    public void warning(String text) { System.out.println("WARNING: " + text); }
    public void severe(String text) { System.out.println("SEVERE: " + text); }
    public void fine(String text) { System.out.println("FINE: " + text); }
    public void finer(String text) { System.out.println("FINER: " + text); }
    public void finest(String text) { System.out.println("FINEST: " + text); }
}
