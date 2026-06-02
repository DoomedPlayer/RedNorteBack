package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.factory.WaitlistFactory;
import BinarySeint.Waitlist_Service.model.RegistroEspera;
import BinarySeint.Waitlist_Service.repository.WaitlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitlistService {

    @Autowired
    private WaitlistRepository repository;

    // Método que usa tu Factory para crear el registro y luego lo guarda en la BD
    public RegistroEspera registrarPaciente(String rut, String especialidad, String tipoAtencion) {
        RegistroEspera nuevoRegistro = WaitlistFactory.crearRegistro(rut, especialidad, tipoAtencion);
        return repository.save(nuevoRegistro);
    }

    // Método que usa tu Repository para traer la lista ordenada por prioridad
    public List<RegistroEspera> obtenerListaPorEspecialidad(String especialidad) {
        return repository.findByEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(especialidad, "EN_ESPERA");
    }
}