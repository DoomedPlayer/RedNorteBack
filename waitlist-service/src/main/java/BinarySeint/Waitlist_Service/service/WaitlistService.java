package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.factory.WaitlistFactoryProvider;
import BinarySeint.Waitlist_Service.model.RegistroEspera;
import BinarySeint.Waitlist_Service.repository.WaitlistRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitlistService {

    @Autowired
    private WaitlistRepository repository;

    @Autowired
    private WaitlistFactoryProvider factoryProvider;

    public RegistroEspera registrarPaciente(String rut, Integer especialidad, String tipoAtencion, boolean gesAuge) {
        WaitlistFactoryMethod fabricaEspecífica = factoryProvider.obtenerFabrica(tipoAtencion);
        RegistroEspera nuevoRegistro = fabricaEspecífica.crearRegistro(rut, especialidad, gesAuge);
        return repository.save(nuevoRegistro);
    }

    public List<RegistroEspera> obtenerListaPorEspecialidad(Integer especialidad) {
        return repository.findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(especialidad, "EN_ESPERA");
    }

    public RegistroEspera obtenerRegistroPorRut(String rutPaciente) {
        return repository.findByRutPaciente(rutPaciente).orElse(null); 
    }

    @Transactional
    public boolean eliminarRegistroPorRut(String rutPaciente) {
        return repository.findByRutPaciente(rutPaciente).map(registro -> {
            repository.delete(registro); 
            return true;
        }).orElse(false); 
    }
}