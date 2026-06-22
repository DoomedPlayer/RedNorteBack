package BinarySeint.Waitlist_Service.factory.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.model.ListaEspera;

@Component
public class UrgenciaFactory implements WaitlistFactoryMethod {

    @Override
    public ListaEspera crearRegistro(String rut, Integer idEspecialidad, boolean gesAuge) {
        ListaEspera registro = new ListaEspera();
        registro.setRutPaciente(rut);
        registro.setIdEspecialidad(idEspecialidad);
        registro.setFechaIngreso(LocalDate.now());

        registro.setTipoAtencion("Urgencia");
        registro.setNivelPrioridad(1);
        registro.setGesAuge(gesAuge);
        return registro;
    }

    @Override
    public String getTipoAtencion() {
        return "URGENCIA";
    }
}
