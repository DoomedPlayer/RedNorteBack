package BinarySeint.Waitlist_Service.factory.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.model.RegistroEspera;

@Component
public class UrgenciaFactory implements WaitlistFactoryMethod {

    @Override
    public RegistroEspera crearRegistro(String rut, Integer idEspecialidad, boolean gesAuge) {
        RegistroEspera registro = new RegistroEspera();
        registro.setRutPaciente(rut);
        registro.setIdEspecialidad(idEspecialidad);
        registro.setFechaIngreso(LocalDate.now());
        registro.setEstado("En espera");
        registro.setGesAuge(gesAuge);

        registro.setTipoAtencion("Urgencia");
        registro.setNivelPrioridad(1);
        
        return registro;
    }

    @Override
    public String getTipoAtencion() {
        return "URGENCIA";
    }
}
