package com.goeuro.makasprzak;

public class CmdLineInterpreter {
    public String from(String[] args) {
        if (args.length < 1) {
            throw new LocationsClientException("Missing location string argument");
        }
        return args[0];
    }
}
