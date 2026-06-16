package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import java.time.LocalDate;

public class WaitlistFactory {

    public static RegistroEspera crearRegistro(String rut, Integer idEspecialidad, String tipoAtencion) {
        RegistroEspera registro = new RegistroEspera();
        registro.setRutPaciente(rut);
        registro.setIdEspecialidad(idEspecialidad);
        registro.setFechaIngreso(LocalDate.now()); 
        registro.setEstado("En espera"); 
        
        switch (tipoAtencion.toUpperCase()) {
            case "URGENCIA":
                registro.setTipoAtencion("Urgencia");
                registro.setNivelPrioridad(1); 
                break;
            case "CIRUGIA":
                registro.setTipoAtencion("Cirugía");
                registro.setNivelPrioridad(2);
                break;
            case "PROCEDIMIENTO":
                registro.setTipoAtencion("Procedimiento");
                registro.setNivelPrioridad(3);
                break;
            case "CONSULTA":
            default:
                registro.setTipoAtencion("Consulta");
                registro.setNivelPrioridad(5);
                break;
        }
        return registro;
    }
}