package com.goeuro.makasprzak;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileWriter {
    public OutputStream openStream(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (IOException e) {
            throw new LocationsClientException("Error writing CSV file", e);
        }
    }
}
