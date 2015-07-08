package com.goeuro.makasprzak;

import java.io.IOException;

public class LocationsClientException extends RuntimeException {
    public LocationsClientException(IOException e) {
        super(e);
    }
}
