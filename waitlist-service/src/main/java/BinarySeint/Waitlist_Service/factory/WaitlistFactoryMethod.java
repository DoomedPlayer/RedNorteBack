package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.model.RegistroEspera;

public interface WaitlistFactoryMethod {
    RegistroEspera crearRegistro(String rut, Integer idEspecialidad, boolean gesAuge);
    String getTipoAtencion();
    
}