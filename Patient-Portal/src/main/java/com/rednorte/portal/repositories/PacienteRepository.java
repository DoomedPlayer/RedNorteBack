package com.rednorte.portal.repositories;

import com.rednorte.portal.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    
    // Spring Boot armará la consulta SQL automáticamente basándose en el nombre de la variable "rutPaciente"
    Optional<Paciente> findByRutPaciente(String rutPaciente);
}