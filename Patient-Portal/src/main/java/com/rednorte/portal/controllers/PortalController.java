package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.PacienteDTO;
import com.rednorte.portal.dtos.CitaMedicaDTO;
import com.rednorte.portal.dtos.DocumentoDTO;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.PacienteRepository;
import com.rednorte.portal.repositories.PersonaRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/portal")
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // =========================================================================
    // 1. ENDPOINT DE INFORMACIÓN PERSONAL (BFF: /api/patients/{id})
    // =========================================================================
    @GetMapping("/pacientes/{rut}")
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
                    .contactoEmergenciaNombre(paciente.getContactoEmergenciaNombre())
                    .contactoEmergenciaParentesco(paciente.getContactoEmergenciaParentesco())
                    .contactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono())
                    .build();

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================================
    // 3. ENDPOINT DE DOCUMENTOS/RECETAS (BFF: /api/v1/portal/pacientes/{id}/documentos)
    // =========================================================================
    @GetMapping("/pacientes/{rut}/documentos")
    public ResponseEntity<List<DocumentoDTO>> obtenerDocumentosPaciente(@PathVariable("rut") String rut) {
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

    @PostMapping("/pacientes/registro-perfil")
    public ResponseEntity<Void> crearPerfilPacienteDesdeAuth(@RequestBody Map<String, String> requestData) {

        Persona persona = Persona.builder()
                .rut(requestData.get("rut"))
                .primerNombre(requestData.get("nombre"))
                .apellidoPaterno(requestData.get("apellidoPaterno"))
                .apellidoMaterno(requestData.get("apellidoMaterno"))
                .email(requestData.get("correo"))
                .telefono(requestData.get("telefono"))
                .build();

        personaRepository.save(persona);
        
        Paciente paciente = Paciente.builder()
                .rutPaciente(requestData.get("rut"))
                .persona(persona)
                .antecedentesMedicos("Sin antecedentes registrados")
                // Mapeamos los datos de emergencia que vienen desde React -> Auth -> Portal
                .contactoEmergenciaNombre(requestData.get("contactoEmergenciaNombre"))
                .contactoEmergenciaParentesco(requestData.get("contactoEmergenciaParentesco"))
                .contactoEmergenciaTelefono(requestData.get("contactoEmergenciaTelefono"))
                .build();

        pacienteRepository.save(paciente);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> obtenerTodosLosPacientes() {
        
        List<Paciente> pacientes = pacienteRepository.findAll();
        List<PacienteDTO> listaResponse = new ArrayList<>();

        for (Paciente p : pacientes) {
            String nombreCompleto = p.getPersona().getPrimerNombre() + " " + 
                                    p.getPersona().getApellidoPaterno();
            
            PacienteDTO dto = PacienteDTO.builder()
                    .rut(p.getRutPaciente())
                    .nombreCompleto(nombreCompleto)
                    .correo(p.getPersona().getEmail())
                    .estadoListaEspera("Activo") 
                    .build();
                    
            listaResponse.add(dto);
        }

        return ResponseEntity.ok(listaResponse);
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