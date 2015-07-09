package com.goeuro.makasprzak;

import java.io.IOException;

public class LocationsClientException extends RuntimeException {

    public LocationsClientException(String message, IOException cause) {
        super(message, cause);
    }

    public LocationsClientException(String message) {
        super(message);
    }
}
