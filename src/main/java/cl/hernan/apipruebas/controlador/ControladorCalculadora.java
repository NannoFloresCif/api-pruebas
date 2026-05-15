package cl.hernan.apipruebas.controlador;

import cl.hernan.apipruebas.servicio.ServicioCalculadora;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ControladorCalculadora {
    private final ServicioCalculadora servicioCalculadora;

    public ControladorCalculadora(ServicioCalculadora servicioCalculadora) {
        this.servicioCalculadora = servicioCalculadora;
    }


    @GetMapping("/api/calculadora/sumar")
    public int sumar(
            @RequestParam int a,
            @RequestParam int b
    ) {
        return servicioCalculadora.sumar(a, b);
    }


}
