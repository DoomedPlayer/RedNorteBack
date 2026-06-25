package com.rednorte.portal.repositories;

import com.rednorte.portal.entities.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    
    List<Documento> findByRutPaciente(String rutPaciente);
}
