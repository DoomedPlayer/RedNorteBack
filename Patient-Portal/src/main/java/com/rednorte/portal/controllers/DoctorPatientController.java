package com.rednorte.portal.controllers;

import com.rednorte.portal.dtos.PacienteRequestDTO;
import com.rednorte.portal.dtos.PacienteResponseDTO;
import com.rednorte.portal.Services.DoctorPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portal/doctor/pacientes")
@CrossOrigin(origins = "*") // Permite peticiones desde React
public class DoctorPatientController {

    @Autowired
    private DoctorPatientService service;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarPacientes() {
        return ResponseEntity.ok(service.obtenerTodosLosPacientes());
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> agregarPaciente(@RequestBody PacienteRequestDTO request) {
        return new ResponseEntity<>(service.crearPaciente(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable String rut) {
        service.eliminarPaciente(rut);
        return ResponseEntity.noContent().build();
    }
}