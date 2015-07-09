package com.goeuro.makasprzak;

import com.google.api.client.http.HttpTransport;

import java.io.InputStream;
import java.io.OutputStream;

public class LocationsClient {

    private final LocationsCsvConverter csvConverter;
    private final LocationsRepository locationsRepository;

    public LocationsClient(LocationsCsvConverter csvConverter, LocationsRepository locationsRepository) {
        this.csvConverter = csvConverter;
        this.locationsRepository = locationsRepository;
    }

    public void writeCsvForLocationString(String locationString, OutputStream os) {
        String escapedString = handlePercentCharacter(locationString);
        InputStream is = locationsRepository.getLocationsJsonStream(escapedString);
        csvConverter.convert(is, os);
    }

    private String handlePercentCharacter(String locationString) {
        return locationString.replaceAll("%","p");
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
                    new LocationsRepository(transport, endpointUrl));
        }
    }
}
