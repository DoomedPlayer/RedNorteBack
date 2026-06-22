package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.model.ListaEspera;

public interface WaitlistFactoryMethod {
    ListaEspera crearRegistro(String rut, Integer idEspecialidad, boolean gesAuge);
    String getTipoAtencion();
    
}