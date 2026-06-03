package BinarySeint.Waitlist_Service.repository;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<RegistroEspera, Long> {
    
    // Actualizado para buscar por IdEspecialidad (Integer) en lugar de un String
    List<RegistroEspera> findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(Integer idEspecialidad, String estado);
}