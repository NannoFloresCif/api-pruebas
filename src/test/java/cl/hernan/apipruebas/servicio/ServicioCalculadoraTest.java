package cl.hernan.apipruebas.servicio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioCalculadoraTest {

    @Test
    void deberiaSumarDosNumerosEnterosPositivos() {
        // Arrange: preparar el objeto que vamos a probar
        ServicioCalculadora servicioCalculadora = new ServicioCalculadora();

        // Act: ejecutar el método que queremos validar
        int resultado = servicioCalculadora.sumar(2, 3);

        // Assert: comparar resultado real contra resultado esperado
        assertEquals(5, resultado);
    }

    @Test
    void deberiaSumarNumerosNegativos() {
        ServicioCalculadora servicioCalculadora = new ServicioCalculadora();

        int resultado = servicioCalculadora.sumar(-2, -3);

        assertEquals(-5, resultado);
    }

    @Test
    void deberiaSumarNumeroPositivoYNegativo() {
        ServicioCalculadora servicioCalculadora = new ServicioCalculadora();

        int resultado = servicioCalculadora.sumar(10, -3);

        assertEquals(7, resultado);
    }
    
}
