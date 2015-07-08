package com.goeuro.makasprzak;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.join;

public class LocationsCsvConverter {

    private static final String HEADER = "_id, name, type, latitude, longitude";
    private final Gson gson = new Gson();

    public void convert(InputStream jsonStream, OutputStream outputStream) throws IOException {
        BufferedWriter writer = createWriter(outputStream);
        JsonReader jsonReader = createReader(jsonStream);
        writeHeader(writer);
        covertLocationsArray(writer, jsonReader);
        jsonReader.close();
        writer.flush();
        writer.close();
    }

    private void writeHeader(BufferedWriter writer) throws IOException {
        writer.append(HEADER);
        writer.newLine();
    }

    private void covertLocationsArray(BufferedWriter writer, JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            convertLocationElement(writer, jsonReader);
        }
    }

    private void convertLocationElement(BufferedWriter writer, JsonReader jsonReader) throws IOException {
        Location location = gson.fromJson(jsonReader, Location.class);
        writer.append(join(", ", toList(location)));
        writer.newLine();
    }

    private JsonReader createReader(InputStream jsonStream) {
        return createJsonReader(jsonStream);
    }

    private BufferedWriter createWriter(OutputStream outputStream) {
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    private List<String> toList(Location location) {
        return Arrays.asList(
                location.getId().toString(),
                escape(location.getName()),
                escape(location.getType()),
                location.getGeoPosition().getLatitude().toString(),
                location.getGeoPosition().getLongitude().toString()
        );
    }

    private String escape(String string) {
        return "\"" + string + "\"";
    }

    private JsonReader createJsonReader(InputStream jsonStream) {
        return new JsonReader(new InputStreamReader(jsonStream));
    }

}
