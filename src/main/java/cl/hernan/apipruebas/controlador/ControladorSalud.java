package cl.hernan.apipruebas.controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ControladorSalud {

    @GetMapping("/salud")
    public String obtenerSalud() {
        return "API funcionando correctamente";
    }
    
}
