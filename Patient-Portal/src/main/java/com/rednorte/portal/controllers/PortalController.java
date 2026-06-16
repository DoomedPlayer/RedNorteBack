package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.ActualizarDatosDTO;
import com.rednorte.portal.dtos.PacienteDTO;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.PacienteRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/portal")
@CrossOrigin(origins = "http://localhost:3000") // Permite que React (puerto 3000) se conecte sin errores de CORS
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // ==========================================
    // 1. OBTENER PERFIL COMPLETO DEL PACIENTE
    // ==========================================
    @GetMapping("/pacientes/{rut}")
    @CircuitBreaker(name = "portalPacienteCB", fallbackMethod = "obtenerPacientePorRutFallback")
    public ResponseEntity<PacienteDTO> obtenerPacientePorRut(@PathVariable String rut) {
        
        // Buscamos al paciente en la base de datos usando el repositorio
        Optional<Paciente> pacienteOpt = pacienteRepository.findByRutPaciente(rut);

        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            Persona persona = paciente.getPersona();

            // Construcción del nombre completo con soporte para segundo nombre opcional
            String nombreCompleto = persona.getPrimerNombre() + " " + 
                                    (persona.getSegundoNombre() != null ? persona.getSegundoNombre() + " " : "") +
                                    persona.getApellidoPaterno() + " " + 
                                    persona.getApellidoMaterno();

            // Mapeo dinámico al DTO incluyendo los nuevos campos de contacto
            PacienteDTO responseDTO = PacienteDTO.builder()
                    .rut(paciente.getRutPaciente())
                    .nombreCompleto(nombreCompleto)
                    .correo(persona.getEmail())
                    .telefono(persona.getTelefono())
                    .direccion(persona.getDireccionTexto())
                    .contactoEmergenciaNombre(persona.getContactoEmergenciaNombre())
                    .contactoEmergenciaTelefono(persona.getContactoEmergenciaTelefono())
                    .estadoListaEspera("Pendiente de asignación médica") 
                    .build();

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // 2. ACTUALIZAR DATOS DE CONTACTO (NUEVO)
    // ==========================================
    @PutMapping("/pacientes/actualizar/{rut}")
    public ResponseEntity<?> actualizarDatosContacto(@PathVariable String rut, @RequestBody ActualizarDatosDTO datosNuevos) {
        
        Optional<Paciente> pacienteOpt = pacienteRepository.findByRutPaciente(rut);
        
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            Persona persona = paciente.getPersona();
            
            if (persona == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error crítico: El paciente no cuenta con una Persona asociada.");
            }
            
            // Seteamos los nuevos valores validados que vienen desde el cliente React
            if (datosNuevos.getCorreo() != null) persona.setEmail(datosNuevos.getCorreo());
            if (datosNuevos.getTelefono() != null) persona.setTelefono(datosNuevos.getTelefono());
            if (datosNuevos.getDireccion() != null) persona.setDireccionTexto(datosNuevos.getDireccion());
            if (datosNuevos.getContactoEmergenciaNombre() != null) persona.setContactoEmergenciaNombre(datosNuevos.getContactoEmergenciaNombre());
            if (datosNuevos.getContactoEmergenciaTelefono() != null) persona.setContactoEmergenciaTelefono(datosNuevos.getContactoEmergenciaTelefono());
            
            // Guardamos el objeto Paciente. Gracias al CascadeType.ALL en Paciente.java, 
            // los cambios bajan automáticamente a la tabla Persona.
            pacienteRepository.save(paciente);
            
            return ResponseEntity.ok("¡Los datos de perfil han sido actualizados con éxito en la base de datos!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // 3. FALLBACK DEL CIRCUIT BREAKER
    // ==========================================
    public ResponseEntity<PacienteDTO> obtenerPacientePorRutFallback(String rut, Throwable t) {
        PacienteDTO dtoContingencia = PacienteDTO.builder()
                .rut(rut)
                .nombreCompleto("Sistema de consulta degradado")
                .correo("N/A")
                .telefono("N/A")
                .direccion("N/A")
                .contactoEmergenciaNombre("N/A")
                .contactoEmergenciaTelefono("N/A")
                .estadoListaEspera("No se pudo verificar el estado debido a intermitencias")
                .build();
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(dtoContingencia);
    }
}