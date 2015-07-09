package com.goeuro.makasprzak;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.goeuro.makasprzak.LocationsClient.Builder.locationsClient;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class LocationsClientTest {

    @Test
    public void shouldSerializeGoEuroResponseToProperCsv_Potsdam() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        locationsClient()
                .withTransport(mockTransport(responseAtPathForUrl("/potsdam.json", HttpTesting.SIMPLE_URL + "Potsdam")))
                .withEndpointUrl(HttpTesting.SIMPLE_URL)
                .build()
                .writeCsvForLocationString("Potsdam", outputStream);
        assertThat(outputStream.toString()).isEqualTo(read("/potsdam.csv"));

    }

    @Test
    public void shouldSerializeGoEuroResponseToProperCsv_Wroclaw() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        locationsClient()
                .withTransport(mockTransport(responseAtPathForUrl("/wroclaw.json", HttpTesting.SIMPLE_URL + "Wroclaw,%20Poland")))
                .withEndpointUrl(HttpTesting.SIMPLE_URL)
                .build()
                .writeCsvForLocationString("Wroclaw, Poland", outputStream);
        assertThat(outputStream.toString()).isEqualTo(read("/wroclaw.csv"));

    }

    @Test
    public void shouldThrowErrorForServiceUnavailable() throws Exception {
        try {
            locationsClient()
                    .withTransport(mockTransport(serviceUnavailable()))
                    .withEndpointUrl(HttpTesting.SIMPLE_URL)
                    .build()
                    .writeCsvForLocationString("any", new ByteArrayOutputStream());
            fail("Expected " + LocationsClientException.class);
        } catch (LocationsClientException e) {
            assertThat(e).hasMessage("503 Service Unavailable");
        }

    }

    @Test
    public void shouldThrowErrorForBadRequest() throws Exception {
        try {
            locationsClient()
                    .withTransport(mockTransport(badRequest()))
                    .withEndpointUrl(HttpTesting.SIMPLE_URL)
                    .build()
                    .writeCsvForLocationString("#$", new ByteArrayOutputStream());
            fail("Expected " + LocationsClientException.class);
        } catch (LocationsClientException e) {
            assertThat(e).hasMessage("400 Bad Request");
        }

    }

    @Test
    public void shouldThrowErrorForMalformedGoEuroResponse() throws Exception {
        try {
            locationsClient()
                    .withTransport(mockTransport(responseForUrl("!@#$%^&&&&*()_+", HttpTesting.SIMPLE_URL + "Wroclaw,%20Poland")))
                    .withEndpointUrl(HttpTesting.SIMPLE_URL)
                    .build()
                    .writeCsvForLocationString("Wroclaw, Poland", new ByteArrayOutputStream());
            fail("Expected " + LocationsClientException.class);
        } catch (LocationsClientException e) {
            assertThat(e).hasMessage("GoEuro returned malformed response");
        }

    }

    @Test
    public void shouldHandlePercentCharacterInQueryString() throws Exception {
        locationsClient()
                .withTransport(mockTransport(responseForUrl("[]", HttpTesting.SIMPLE_URL + "Quality%20100p")))
                .withEndpointUrl(HttpTesting.SIMPLE_URL)
                .build()
                .writeCsvForLocationString("Quality 100%", new ByteArrayOutputStream());
    }

    private Executor serviceUnavailable() {
        return any -> new MockLowLevelHttpResponse().setStatusCode(503).setReasonPhrase("Service Unavailable");
    }
    private Executor badRequest() {
        return any -> new MockLowLevelHttpResponse().setStatusCode(400).setReasonPhrase("Bad Request");
    }

    private Executor responseAtPathForUrl(String expectedResponsePath, String expectedUrl) {
        return actual -> {
                if (expectedUrl.equals(actual))
                    return new MockLowLevelHttpResponse()
                            .setContentType(Json.MEDIA_TYPE)
                            .setContent(read(expectedResponsePath));
                else
                    return new MockLowLevelHttpResponse().setStatusCode(404);
            };
    }
    private Executor responseForUrl(String expectedResponse, String expectedUrl) {
        return actual -> {
                if (expectedUrl.equals(actual))
                    return new MockLowLevelHttpResponse()
                            .setContentType(Json.MEDIA_TYPE)
                            .setContent(expectedResponse);
                else
                    return new MockLowLevelHttpResponse().setStatusCode(404);
            };
    }

    private MockHttpTransport mockTransport(final Executor executor) {
        return new MockHttpTransport() {
                @Override
                public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                    return new MockLowLevelHttpRequest() {
                        @Override
                        public LowLevelHttpResponse execute() throws IOException {
                            return executor.execute(url);
                        }
                    };
                }
            };
    }

    private String read(String path) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(path));
    }

    private static interface Executor {
        MockLowLevelHttpResponse execute(String expectedRequest) throws IOException;
    }
}