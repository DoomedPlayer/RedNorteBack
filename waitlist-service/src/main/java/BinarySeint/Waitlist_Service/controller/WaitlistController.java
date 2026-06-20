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
@RequestMapping("/api/espera")
public class WaitlistController {

    @Autowired
    private WaitlistService service;

    @PostMapping("/registrar")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "registrarPacienteFallback")
    public ResponseEntity<RegistroEspera> registrarPaciente(@RequestBody Map<String, Object> request) {
        Integer idEspecialidad = Integer.parseInt(request.get("idEspecialidad").toString());
        boolean gesAuge = request.get("gesAuge") != null && Boolean.parseBoolean(request.get("gesAuge").toString());
        
        RegistroEspera registrado = service.registrarPaciente(
                request.get("rutPaciente").toString(),
                idEspecialidad,
                request.get("tipoAtencion").toString(),
                gesAuge
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/paciente/{rut}")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "obtenerEsperaPacienteFallback")
    public ResponseEntity<RegistroEspera> obtenerEsperaPorPaciente(@PathVariable("rut") String rut) {
        RegistroEspera registro = service.obtenerRegistroPorRut(rut);
        
        if (registro != null) {
            return ResponseEntity.ok(registro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/paciente/{rut}")
    public ResponseEntity<Void> eliminarDeListaEspera(@PathVariable("rut") String rut) {
        boolean eliminado = service.eliminarRegistroPorRut(rut);
        
        if (eliminado) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    // Fallback específico para la consulta por paciente
    public ResponseEntity<RegistroEspera> obtenerEsperaPacienteFallback(String rut, Throwable t) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
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