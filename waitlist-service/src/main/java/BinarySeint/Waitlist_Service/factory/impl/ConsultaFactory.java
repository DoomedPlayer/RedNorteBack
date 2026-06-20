package BinarySeint.Waitlist_Service.factory.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.model.RegistroEspera;

@Component
public class ConsultaFactory implements WaitlistFactoryMethod {

    @Override
    public RegistroEspera crearRegistro(String rut, Integer idEspecialidad, boolean gesAuge) {
        RegistroEspera registro = new RegistroEspera();
        registro.setRutPaciente(rut);
        registro.setIdEspecialidad(idEspecialidad);
        registro.setFechaIngreso(LocalDate.now());
        registro.setEstado("En espera");
        registro.setGesAuge(gesAuge);
        
        // Configuración específica de Cirugía
        registro.setTipoAtencion("Consulta");
        registro.setNivelPrioridad(5);
        
        return registro;
    }

    @Override
    public String getTipoAtencion() {
        return "CONSULTA";
    }
}
