package BinarySeint.Waitlist_Service.repository;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<RegistroEspera, Long> {
    
    List<RegistroEspera> findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(Integer idEspecialidad, String estado);
    Optional<RegistroEspera> findByRutPaciente(String rutPaciente);
}