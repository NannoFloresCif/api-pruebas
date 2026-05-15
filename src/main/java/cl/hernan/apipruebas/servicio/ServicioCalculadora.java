package cl.hernan.apipruebas.servicio;


import org.springframework.stereotype.Service;

@Service
public class ServicioCalculadora {
    public int sumar(int numeroA, int numeroB) {
        return numeroA + numeroB;
    }

}
