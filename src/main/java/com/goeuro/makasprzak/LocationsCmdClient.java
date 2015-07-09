package com.goeuro.makasprzak;

import com.google.inject.Inject;

public class LocationsCmdClient {
    public static final String MESSAGE_SUFFIX = " successfully created";
    private final CmdLineInterpreter cmdLineInterpreter;
    private final FileNameProvider fileNameProvider;
    private final FileWriter fileWriter;
    private final LocationsClient client;

    @Inject
    public LocationsCmdClient(
            CmdLineInterpreter cmdLineInterpreter,
            FileNameProvider fileNameProvider,
            FileWriter fileWriter,
            LocationsClient client) {
        this.cmdLineInterpreter = cmdLineInterpreter;
        this.fileNameProvider = fileNameProvider;
        this.fileWriter = fileWriter;
        this.client = client;
    }

    public String execute(String[] args) {
        try {
            String locationString = cmdLineInterpreter.from(args);
            String fileName = fileNameProvider.fileNameFor(locationString);
            client.writeCsvStreamForLocationString(
                    fileWriter.openStream(
                            fileName),
                    locationString);
            return fileName + MESSAGE_SUFFIX;
        } catch (LocationsClientException e) {
            return e.getMessage();
        }
    }
}
