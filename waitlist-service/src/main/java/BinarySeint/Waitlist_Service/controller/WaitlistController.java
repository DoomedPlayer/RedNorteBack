package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.EstadoPaciente;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
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

    @PostMapping("/paciente")
    public ResponseEntity<RegistroPaciente> crearRegistroPaciente(@RequestBody Map<String, Object> request) {
        String rut = request.get("rutPaciente").toString();
        
        // Si no envían estado, asumimos SIN_REGISTROS por defecto
        String estadoStr = request.get("estado") != null ? request.get("estado").toString() : "SIN_REGISTROS";
        EstadoPaciente estado = EstadoPaciente.valueOf(estadoStr);
        
        String prioridad = request.get("prioridad") != null ? request.get("prioridad").toString() : "Sin prioridad";
        boolean gesAuge = request.get("gesAuge") != null && Boolean.parseBoolean(request.get("gesAuge").toString());
        
        RegistroPaciente registrado = service.guardarRegistroPaciente(rut, estado, prioridad, gesAuge);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/paciente/{rut}")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "obtenerEsperaPacienteFallback")
    public ResponseEntity<RegistroPaciente> obtenerEsperaPorPaciente(@PathVariable("rut") String rut) {
        RegistroPaciente registro = service.obtenerRegistroPorRut(rut);
        if (registro != null) {
            return ResponseEntity.ok(registro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/paciente/{rut}/estado")
    public ResponseEntity<RegistroPaciente> modificarEstadoPorDoctor(
            @PathVariable("rut") String rut, 
            @RequestBody Map<String, String> request) {
        try {
            EstadoPaciente nuevoEstado = EstadoPaciente.valueOf(request.get("estado"));
            String nuevaPrioridad = request.get("prioridad"); 
            
            RegistroPaciente actualizado = service.modificarRegistroPorDoctor(rut, nuevoEstado, nuevaPrioridad);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/lista")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "registrarListaFallback")
    public ResponseEntity<RegistroPaciente> registrarEnListaEspera(@RequestBody Map<String, Object> request) {
        Integer idEspecialidad = Integer.parseInt(request.get("idEspecialidad").toString());
        boolean gesAuge = request.get("gesAuge") != null && Boolean.parseBoolean(request.get("gesAuge").toString());
        
        // Crea el ticket de lista de espera y sincroniza el RegistroPaciente a "EN_ESPERA"
        RegistroPaciente registrado = service.registrarEnListaEspera(
                request.get("rutPaciente").toString(),
                idEspecialidad,
                request.get("tipoAtencion").toString(),
                gesAuge
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/lista/{idEspecialidad}")
    public ResponseEntity<List<ListaEspera>> obtenerLista(@PathVariable("idEspecialidad") Integer idEspecialidad) {
        List<ListaEspera> lista = service.obtenerListaPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/lista/{rut}")
    public ResponseEntity<Void> eliminarDeListaEspera(@PathVariable("rut") String rut) {
        // Esto lo saca de la fila y le actualiza el estado a "HORA_ASIGNADA"
        boolean eliminado = service.eliminarRegistroPorRut(rut);
        if (eliminado) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @GetMapping("/siguiente/{especialidad}")
    public String obtenerSiguientePaciente(@PathVariable("especialidad") String especialidad) {
        return service.obtenerYSacarSiguientePaciente(especialidad);
    }

    public ResponseEntity<RegistroPaciente> registrarListaFallback(Map<String, Object> request, Throwable t) {
        System.err.println("Fallback activado al registrar en lista: " + t.getMessage());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<RegistroPaciente> obtenerEsperaPacienteFallback(String rut, Throwable t) {
        System.err.println("Fallback activado al obtener datos del paciente: " + t.getMessage());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}