package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Medico;
import com.rednorte.portal.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/portal/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/{rut}")
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