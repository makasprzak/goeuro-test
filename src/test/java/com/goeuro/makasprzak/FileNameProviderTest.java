package com.goeuro.makasprzak;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FileNameProviderTest {

    @Test
    public void shouldReplaceIllegalCharactersWith_() throws Exception {
        assertThat(new FileNameProvider().fileNameFor("V!e@r#y% p^o&l*u;t.e,d s?t/r~i=n}g..."))
                .isEqualTo("V_e_r_y__p_o_l_u_t_e_d_s_t_r_i_n_g___.csv");

    }
}