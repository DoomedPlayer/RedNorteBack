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

    public RegistroEspera registrarPaciente(String rut, Integer idEspecialidad, String tipoAtencion) {
        RegistroEspera nuevoRegistro = WaitlistFactory.crearRegistro(rut, idEspecialidad, tipoAtencion);
        return repository.save(nuevoRegistro);
    }

    public List<RegistroEspera> obtenerListaPorEspecialidad(Integer idEspecialidad) {
        return repository.findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(idEspecialidad, "En espera");
    }
}