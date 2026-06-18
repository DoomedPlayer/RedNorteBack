package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import BinarySeint.Waitlist_Service.service.WaitlistService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Alineado con la app React
@RequestMapping("/api/espera")
public class WaitlistController {

    @Autowired
    private WaitlistService service;

    @PostMapping("/registrar")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "registrarPacienteFallback")
    public ResponseEntity<RegistroEspera> registrarPaciente(@RequestBody Map<String, Object> request) {
        Integer idEspecialidad = Integer.parseInt(request.get("idEspecialidad").toString());
        
        RegistroEspera registrado = service.registrarPaciente(
                request.get("rutPaciente").toString(),
                idEspecialidad,
                request.get("tipoAtencion").toString()
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/lista/{idEspecialidad}")
    public ResponseEntity<List<RegistroEspera>> obtenerLista(@PathVariable("idEspecialidad") Integer idEspecialidad) {
        List<RegistroEspera> lista = service.obtenerListaPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(lista);
    }

    // Corregido: Se agregó Throwable t para cumplir con la firma requerida por Resilience4j
    public ResponseEntity<RegistroEspera> registrarPacienteFallback(Map<String, Object> request, Throwable t) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}