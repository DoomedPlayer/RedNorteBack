package BinarySeint.Waitlist_Service.repository;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<RegistroEspera, Long> {
    
    // Spring Data JPA crea la query automáticamente con el nombre del método:
    // Retorna pacientes en espera, ordenados primero por prioridad (1 es más urgente) y luego por fecha de llegada.
    List<RegistroEspera> findByEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(String especialidad, String estado);
    
    List<RegistroEspera> findByRutPaciente(String rutPaciente);
}