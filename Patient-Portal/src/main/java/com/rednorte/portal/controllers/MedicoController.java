package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Medico;
import com.rednorte.portal.repositories.MedicoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/portal/medicos")
@Tag(name = "Portal Médico", description = "Consultas sobre la plantilla de profesionales de salud")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/{rut}")
    @Operation(summary = "Obtener datos del médico", description = "Mapea y retorna la información pública del doctor y su especialidad.")
    @ApiResponse(responseCode = "200", description = "Médico encontrado")
    @ApiResponse(responseCode = "404", description = "Médico no existe en los registros")
    public ResponseEntity<Map<String, Object>> obtenerMedicoPorRut(@PathVariable("rut") String rut) {
        return medicoRepository.findById(rut).map(medico -> {
            Map<String, Object> response = new HashMap<>();
            response.put("rut", medico.getRutMedico());
            response.put("nombre", "Dr. " + medico.getPersona().getPrimerNombre() + " " + medico.getPersona().getApellidoPaterno());
            response.put("especialidad", medico.getEspecialidad());
            response.put("idEspecialidad", medico.getIdEspecialidad());
            
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
}