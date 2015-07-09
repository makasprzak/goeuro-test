package com.goeuro.makasprzak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileOutputStream;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LocationsCmdClientTest {

    @Spy private CmdLineInterpreter cmdLineInterpreter = new CmdLineInterpreter();
    @Spy private FileNameProvider fileNameProvider = new FileNameProvider();
    @Mock private FileWriter fileWriter;
    @Mock private LocationsClient locationsClient;

    @InjectMocks private LocationsCmdClient locationsCmdClient;

    @Mock private FileOutputStream fos;

    @Test
    public void shouldExecuteLocationClient() throws Exception {
        given(fileWriter.openStream("test.csv")).willReturn(fos);
        assertThat(locationsCmdClient.execute(new String[]{"test"}))
                .isEqualTo("test.csv successfully created");
        verify(locationsClient).writeCsvStreamForLocationString(fos, "test");
    }

    @Test
    public void shouldReturnErrorMessageForIllegalCmdLine() throws Exception {
        assertThat(locationsCmdClient.execute(new String[]{}))
                .isEqualTo("Missing location string argument");
        verify(locationsClient, never()).writeCsvStreamForLocationString(fos, "test");
    }

    @Test
    public void shouldReturnErrorMessageForFileWritingError() throws Exception {
        given(fileWriter.openStream("test.csv")).willThrow(new LocationsClientException("Some IO problem occured"));
        assertThat(locationsCmdClient.execute(new String[]{"test"}))
                .isEqualTo("Some IO problem occured");
        verify(locationsClient, never()).writeCsvStreamForLocationString(fos, "test");
    }
}