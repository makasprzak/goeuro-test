package com.goeuro.makasprzak;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocationsClient {

    private final LocationsCsvConverter csvConverter;
    private final HttpTransport transport;
    private final String endpointUrl;

    public LocationsClient(LocationsCsvConverter csvConverter, HttpTransport transport, String endpointUrl) {
        this.csvConverter = csvConverter;
        this.transport = transport;
        this.endpointUrl = endpointUrl;
    }

    public void writeCsvForLocationString(String locationString, OutputStream os) {
        try {
            //TODO refactor - extract LocationsHttpResource together with exception Handling
            InputStream is = transport.createRequestFactory()
                    .buildGetRequest(getUrl(locationString))
                    .execute().getContent();
            //TODO handle exception within converterm
            csvConverter.convert(is, os);
        } catch (HttpResponseException e) {
            throw new LocationsClientException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GenericUrl getUrl(String locationString) {
        return new GenericUrl(endpointUrl + locationString);
    }

    public static interface TransportStep {
        EndpointUrlStep withTransport(HttpTransport transport);
    }

    public static interface EndpointUrlStep {
        BuildStep withEndpointUrl(String endpointUrl);
    }

    public static interface BuildStep {
        LocationsClient build();
    }


    public static class Builder implements TransportStep, EndpointUrlStep, BuildStep {
        private HttpTransport transport;
        private String endpointUrl;

        private Builder() {
        }

        public static TransportStep locationsClient() {
            return new Builder();
        }

        @Override
        public EndpointUrlStep withTransport(HttpTransport transport) {
            this.transport = transport;
            return this;
        }

        @Override
        public BuildStep withEndpointUrl(String endpointUrl) {
            this.endpointUrl = endpointUrl;
            return this;
        }

        @Override
        public LocationsClient build() {
            return new LocationsClient(
                    new LocationsCsvConverter(),
                    this.transport,
                    this.endpointUrl
            );
        }
    }
}
