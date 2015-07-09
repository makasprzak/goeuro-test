package com.goeuro.makasprzak;

public class FileNameProvider {

    public static final String CSV = ".csv";

    public String fileNameFor(String in) {
        return santize(in) + CSV;
    }

    private String santize(String in) {
        return in.replaceAll("[^\\w]","_");
    }
}
