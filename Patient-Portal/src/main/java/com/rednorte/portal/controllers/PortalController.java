package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.PacienteDTO;
import com.rednorte.portal.dtos.CitaMedicaDTO;
import com.rednorte.portal.dtos.DocumentoDTO;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.repositories.PacienteRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Permite conexión directa con React
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // =========================================================================
    // 1. ENDPOINT DE INFORMACIÓN PERSONAL (BFF: /api/patients/{id})
    // =========================================================================
    @GetMapping("/api/patients/{rut}")
    @CircuitBreaker(name = "portalPacienteCB", fallbackMethod = "obtenerPacientePorRutFallback")
    public ResponseEntity<PacienteDTO> obtenerPacientePorRut(@PathVariable("rut") String rut) {
        
        Optional<Paciente> pacienteOpt = pacienteRepository.findByRutPaciente(rut);

        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();

            String nombreCompleto = paciente.getPersona().getPrimerNombre() + " " + 
                                    paciente.getPersona().getApellidoPaterno() + " " +
                                    paciente.getPersona().getApellidoMaterno();

            PacienteDTO responseDTO = PacienteDTO.builder()
                    .rut(paciente.getRutPaciente())
                    .nombreCompleto(nombreCompleto)
                    .correo(paciente.getPersona().getEmail())
                    .estadoListaEspera("Pendiente de asignación médica") 
                    .build();

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================================
    // 2. ENDPOINT DE CITAS MÉDICAS (BFF: /api/v1/portal/pacientes/{id}/citas)
    // =========================================================================
    @GetMapping("/api/v1/portal/pacientes/{rut}/citas")
    public ResponseEntity<List<CitaMedicaDTO>> obtenerCitasPaciente(@PathVariable String rut) {
        List<CitaMedicaDTO> citas = new ArrayList<>();
        
        // TODO: Aquí debes llamar a tu repositorio real de citas filtrando por el RUT
        // Ejemplo de datos de prueba para la vista del profesor:
        /*
        citas.add(CitaMedicaDTO.builder()
                .especialidad("Cardiología")
                .doctor("Dr. Alejandro Sanz")
                .fecha("20-05-2026 10:00 a. m.")
                .box("Box A-12")
                .build());
        */
        
        return ResponseEntity.ok(citas);
    }

    // =========================================================================
    // 3. ENDPOINT DE DOCUMENTOS/RECETAS (BFF: /api/v1/portal/pacientes/{id}/documentos)
    // =========================================================================
    @GetMapping("/api/v1/portal/pacientes/{rut}/documentos")
    public ResponseEntity<List<DocumentoDTO>> obtenerDocumentosPaciente(@PathVariable String rut) {
        List<DocumentoDTO> documentos = new ArrayList<>();
        
        // TODO: Aquí debes llamar a tu repositorio real de recetas/exámenes por RUT
        // Ejemplo de datos de prueba:
        /*
        documentos.add(DocumentoDTO.builder()
                .tipo("Receta Médica Electrónica")
                .descripcion("Tratamiento Crónico")
                .fecha("01-06-2026")
                .build());
        */
        
        return ResponseEntity.ok(documentos);
    }

    // Fallback de Circuit Breaker para el perfil básico
    public ResponseEntity<PacienteDTO> obtenerPacientePorRutFallback(String rut, Throwable t) {
        PacienteDTO dtoContingencia = PacienteDTO.builder()
                .rut(rut)
                .nombreCompleto("Sistema de consulta degradado")
                .correo("N/A")
                .estadoListaEspera("No se pudo verificar el estado debido a intermitencias")
                .build();
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(dtoContingencia);
    }
}