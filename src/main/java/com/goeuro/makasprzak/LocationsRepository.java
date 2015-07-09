package com.goeuro.makasprzak;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.IOException;
import java.io.InputStream;

public class LocationsRepository {

    private final HttpTransport transport;
    private final String endpointUrl;

    @Inject
    public LocationsRepository(HttpTransport transport, @Named("endpointUrl") String endpointUrl) {
        this.transport = transport;
        this.endpointUrl = endpointUrl;
    }

    public InputStream getLocationsJsonStream(String locationString) {
        try {
            HttpResponse response = transport.createRequestFactory()
                    .buildGetRequest(getUrl(locationString))
                    .execute();
            if (response == null) {
                throw new LocationsClientException("No Response");
            }
            return response.getContent();
        } catch (IOException e) {
            throw new LocationsClientException(e.getMessage(), e);
        }
    }

    private GenericUrl getUrl(String locationString) {
        return new GenericUrl(endpointUrl + locationString);
    }

}
