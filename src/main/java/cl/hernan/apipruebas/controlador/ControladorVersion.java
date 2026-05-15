package cl.hernan.apipruebas.controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorVersion {

    @GetMapping("/api/version")
    public String obtenerVersion() {
        return "v1.0.0";
    }
    
}
