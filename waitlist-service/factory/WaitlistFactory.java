package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import java.time.LocalDateTime;

public class WaitlistFactory {

    public static RegistroEspera crearRegistro(String rut, String especialidad, String tipoAtencion) {
        RegistroEspera registro = new RegistroEspera();
        registro.setRutPaciente(rut);
        registro.setEspecialidad(especialidad);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("EN_ESPERA");
        
        // El Factory define la prioridad base dependiendo del tipo de atención
        switch (tipoAtencion.toUpperCase()) {
            case "URGENCIA":
                registro.setTipoAtencion("URGENCIA");
                registro.setNivelPrioridad(1); 
                break;
            case "CIRUGIA":
                registro.setTipoAtencion("CIRUGIA");
                registro.setNivelPrioridad(2);
                break;
            case "PROCEDIMIENTO":
                registro.setTipoAtencion("PROCEDIMIENTO");
                registro.setNivelPrioridad(3);
                break;
            case "CONSULTA":
            default:
                registro.setTipoAtencion("CONSULTA");
                registro.setNivelPrioridad(5); // Prioridad más baja
                break;
        }
        return registro;
    }
}