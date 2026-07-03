package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.PacienteDTO;
import com.rednorte.portal.dtos.CitaMedicaDTO;
import com.rednorte.portal.dtos.DocumentoDTO;
import com.rednorte.portal.entities.Documento;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.entities.TipoPrevision;
import com.rednorte.portal.repositories.DocumentoRepository;
import com.rednorte.portal.repositories.PacienteRepository;
import com.rednorte.portal.repositories.PersonaRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/portal")
@Tag(name = "Portal Paciente", description = "Gestión de perfiles, antecedentes y documentos clínicos de los pacientes")
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private EntityManager entityManager;


    @GetMapping("/pacientes/{rut}")
    @CircuitBreaker(name = "portalPacienteCB", fallbackMethod = "obtenerPacientePorRutFallback")
    @Operation(summary = "Obtener perfil del paciente", description = "Retorna los datos personales consolidados del paciente usando su RUT.")
    @ApiResponse(responseCode = "200", description = "Perfil encontrado")
    @ApiResponse(responseCode = "404", description = "Paciente no registrado")
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
                    .edad(paciente.getPersona().getEdad()) 
                    .prevision(paciente.getPrevision() != null ? paciente.getPrevision().name() : "NO REGISTRADA")
                    .esGes(paciente.getEsGes() != null ? paciente.getEsGes() : false)
                    .estadoListaEspera("Pendiente de asignación médica")
                    .antecedentesMedicos(paciente.getAntecedentesMedicos() != null ? paciente.getAntecedentesMedicos() : "Sin antecedentes registrados")
                    .contactoEmergenciaNombre(paciente.getContactoEmergenciaNombre())
                    .contactoEmergenciaParentesco(paciente.getContactoEmergenciaParentesco())
                    .contactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono())
                    .build();

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pacientes/{rut}/documentos")
    @Operation(summary = "Obtener documentos médicos", description = "Retorna el historial de recetas y exámenes del paciente.")
    public ResponseEntity<List<DocumentoDTO>> obtenerDocumentosPaciente(@PathVariable("rut") String rut) {        
        List<Documento> documentosDB = documentoRepository.findByRutPaciente(rut);

        List<DocumentoDTO> documentosDTO = new ArrayList<>();

        for (Documento doc : documentosDB) {
            DocumentoDTO dto = new DocumentoDTO(
                doc.getNombreDocumento(),
                doc.getEmisorYFecha(),
                doc.getUrlDescarga()
            );
            documentosDTO.add(dto);
        }

        return ResponseEntity.ok(documentosDTO);
    }

    @Transactional
    @PostMapping("/pacientes/registro-perfil")
    @Operation(summary = "Sincronizar perfil desde Auth", description = "Endpoint interno llamado por Security para inicializar los datos demográficos tras el registro.")
    public ResponseEntity<Void> crearPerfilPacienteDesdeAuth(@RequestBody Map<String, Object> requestData) { 

        Integer edadPaciente = null;
        if (requestData.get("edad") != null && !requestData.get("edad").toString().isEmpty()) {
            edadPaciente = Integer.parseInt(requestData.get("edad").toString());
        }

        Persona persona = Persona.builder()
                .rut(requestData.get("rut") != null ? requestData.get("rut").toString() : "")
                .primerNombre(requestData.get("nombre") != null ? requestData.get("nombre").toString() : "Sin Nombre")
                .apellidoPaterno(requestData.get("apellidoPaterno") != null ? requestData.get("apellidoPaterno").toString() : "")
                .apellidoMaterno(requestData.get("apellidoMaterno") != null ? requestData.get("apellidoMaterno").toString() : "")
                .email(requestData.get("correo") != null ? requestData.get("correo").toString() : "")
                .telefono(requestData.get("telefono") != null ? requestData.get("telefono").toString() : "")
                .edad(edadPaciente)
                .build();

        entityManager.persist(persona);
        
        String antecedentes = "Sin antecedentes registrados";
        if (requestData.get("antecedentesMedicos") != null && !requestData.get("antecedentesMedicos").toString().trim().isEmpty()) {
            antecedentes = requestData.get("antecedentesMedicos").toString();
        }

        boolean esGes = false;
        if (requestData.get("esGes") != null) {
            esGes = Boolean.parseBoolean(requestData.get("esGes").toString());
        }

        TipoPrevision tipoPrevision = null;
        if (requestData.get("prevision") != null && !requestData.get("prevision").toString().isEmpty()) {
            try {
                tipoPrevision = TipoPrevision.valueOf(requestData.get("prevision").toString());
            } catch (IllegalArgumentException e) {
                System.err.println("Previsión no válida: " + requestData.get("prevision"));
            }
        }
        
        Paciente paciente = Paciente.builder()
                .rutPaciente(persona.getRut()) 
                .persona(persona)
                .antecedentesMedicos(antecedentes)
                .esGes(esGes)
                .prevision(tipoPrevision)
                .contactoEmergenciaNombre(requestData.get("contactoEmergenciaNombre") != null ? requestData.get("contactoEmergenciaNombre").toString() : "No registrado")
                .contactoEmergenciaParentesco(requestData.get("contactoEmergenciaParentesco") != null ? requestData.get("contactoEmergenciaParentesco").toString() : "No registrado")
                .contactoEmergenciaTelefono(requestData.get("contactoEmergenciaTelefono") != null ? requestData.get("contactoEmergenciaTelefono").toString() : "No registrado")
                .build();

        entityManager.persist(paciente);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pacientes")
    @Operation(summary = "Listar todos los pacientes", description = "Retorna el registro completo de pacientes inscritos.")
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
                    .edad(p.getPersona().getEdad())
                    .prevision(p.getPrevision() != null ? p.getPrevision().name() : "NO REGISTRADA")
                    .esGes(p.getEsGes() != null ? p.getEsGes() : false)
                    .antecedentesMedicos(p.getAntecedentesMedicos() != null ? p.getAntecedentesMedicos() : "Sin antecedentes registrados")
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