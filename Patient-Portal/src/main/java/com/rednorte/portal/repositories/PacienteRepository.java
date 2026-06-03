package com.rednorte.portal.repositories;

import com.rednorte.portal.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    // ¡Magia de Spring Boot! 
    // Solo con nombrar el método así, Spring sabe que debe hacer un "SELECT * FROM pacientes WHERE rut = ?"
    Optional<Paciente> findByRut(String rut);
    
}