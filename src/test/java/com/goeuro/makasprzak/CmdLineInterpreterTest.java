package com.goeuro.makasprzak;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CmdLineInterpreterTest {

    @Test
    public void shouldInterpretFirstArgumentAsLocationString() throws Exception {
        assertThat(new CmdLineInterpreter().from(new String[]{"STRING"}))
                .isEqualTo("STRING");
    }

    @Test
    public void shouldInterpretFirstArgumentAsLocationString2() throws Exception {
        assertThat(new CmdLineInterpreter().from(new String[]{"STRING2"}))
                .isEqualTo("STRING2");
    }

    @Test(expected = LocationsClientException.class)
    public void shouldThrowErrorForMissingArgument() throws Exception {
        new CmdLineInterpreter().from(new String[]{});
    }
}