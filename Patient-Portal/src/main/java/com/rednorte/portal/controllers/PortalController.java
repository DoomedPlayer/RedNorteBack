package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.PacienteDTO;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/portal")
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("{\"mensaje\": \"BFF Portal del Paciente funcionando\"}");
    }

    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteRepository.save(paciente));
    }

    // ¡MÉTODO ACTUALIZADO! Ahora devuelve un PacienteDTO
    @GetMapping("/pacientes/{rut}")
    public ResponseEntity<PacienteDTO> buscarPorRut(@PathVariable String rut) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByRut(rut);
        
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            
            // TRANSFRORMACIÓN: Pasamos de Entidad a DTO (Pulimos los datos)
            PacienteDTO dtoRespuesta = PacienteDTO.builder()
                    .rut(paciente.getRut())
                    .nombreCompleto(paciente.getNombre() + " " + paciente.getApellidos()) // Juntamos los strings
                    .correo(paciente.getCorreo())
                    .alertasActivas(paciente.getNotificacionesActivas())
                    .estadoListaEspera("Pendiente de asignación médica") // Dato estratégico para el Frontend
                    .build();
            
            return ResponseEntity.ok(dtoRespuesta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}