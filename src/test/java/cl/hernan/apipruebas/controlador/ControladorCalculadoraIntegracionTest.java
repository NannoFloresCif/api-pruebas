package cl.hernan.apipruebas.controlador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ControladorCalculadoraIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deberiaResponderResultadoDeSumaDesdeEndpoint() throws Exception {
        mockMvc.perform(get("/api/calculadora/sumar")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("5")));
    }
    
}
