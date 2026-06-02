package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/portal")
public class PortalController {

    // Conectamos a nuestro "bibliotecario" de la base de datos
    @Autowired
    private PacienteRepository pacienteRepository;

    // 1. El endpoint de prueba que ya tenías
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("{\"mensaje\": \"BFF Portal del Paciente funcionando correctamente\"}");
    }

    // 2. NUEVO: Endpoint para REGISTRAR un paciente (POST)
    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        // Guarda el paciente en MySQL y lo devuelve con su ID generado
        Paciente nuevoPaciente = pacienteRepository.save(paciente);
        return ResponseEntity.ok(nuevoPaciente);
    }

    // 3. NUEVO: Endpoint para BUSCAR un paciente por RUT (GET)
    @GetMapping("/pacientes/{rut}")
    public ResponseEntity<Paciente> buscarPorRut(@PathVariable String rut) {
        // Usa la magia de Spring Data JPA que configuramos en el Repositorio
        Optional<Paciente> paciente = pacienteRepository.findByRut(rut);
        
        if (paciente.isPresent()) {
            return ResponseEntity.ok(paciente.get()); // Si lo encuentra, devuelve status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Si no existe, devuelve status 404 Not Found
        }
    }
}