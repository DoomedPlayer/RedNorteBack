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
@CrossOrigin(origins = "http://localhost:3000") // Permite que React (puerto 3000) se conecte sin errores de CORS
public class PortalController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/pacientes/{rut}")
    public ResponseEntity<PacienteDTO> obtenerPacientePorRut(@PathVariable String rut) {
        
        // 1. Buscamos al paciente en la base de datos usando el nuevo método del Repositorio
        Optional<Paciente> pacienteOpt = pacienteRepository.findByRutPaciente(rut);

        // 2. Si existe, armamos la respuesta
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();

            // Nota cómo ahora sacamos el nombre y el correo desde paciente.getPersona()
            String nombreCompleto = paciente.getPersona().getPrimerNombre() + " " + 
                                    paciente.getPersona().getApellidoPaterno() + " " +
                                    paciente.getPersona().getApellidoMaterno();

            PacienteDTO responseDTO = PacienteDTO.builder()
                    .rut(paciente.getRutPaciente())
                    .nombreCompleto(nombreCompleto)
                    .correo(paciente.getPersona().getEmail())
                    // Por ahora dejamos este dato en duro, luego lo podemos sacar de la tabla registro_espera
                    .estadoListaEspera("Pendiente de asignación médica") 
                    .build();

            return ResponseEntity.ok(responseDTO);
        } else {
            // Si no se encuentra el RUT, devolvemos un error 404
            return ResponseEntity.notFound().build();
        }
    }
}