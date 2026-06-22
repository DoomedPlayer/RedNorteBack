package BinarySeint.AutoReasign_Service.controller;

import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.service.CitaMedicaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaMedicaController {

    @Autowired
    private CitaMedicaService citaMedicaService;

    @PostMapping
    @CircuitBreaker(name = "citaMedicaCB", fallbackMethod = "crearCitaFallback")
    public ResponseEntity<CitaMedica> crearCita(@RequestBody CitaMedica cita) {
        CitaMedica nuevaCita = citaMedicaService.crearCita(cita);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CitaMedica>> obtenerTodas() {
        List<CitaMedica> citas = citaMedicaService.obtenerTodasLasCitas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<CitaMedica>> obtenerCitasPorPaciente(@PathVariable("rut") String rut) {
        List<CitaMedica> citas = citaMedicaService.obtenerCitasPorRutPaciente(rut);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> obtenerPorId(@PathVariable("id") Long id) {
        CitaMedica cita = citaMedicaService.obtenerCitaPorId(id);
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/especialidad/{nombre}")
    public ResponseEntity<List<CitaMedica>> obtenerCitasPorEspecialidad(@PathVariable("nombre") String nombre) {
        List<CitaMedica> citas = citaMedicaService.obtenerCitasPorEspecialidad(nombre);
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> actualizarCita(@PathVariable("id") Long id, @RequestBody CitaMedica datosActualizados) {
        CitaMedica citaEditada = citaMedicaService.actualizarCita(id, datosActualizados);
        return ResponseEntity.ok(citaEditada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable("id") Long id) {
        citaMedicaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    @CircuitBreaker(name = "citaMedicaCB", fallbackMethod = "cancelarCitaFallback")
    public ResponseEntity<String> cancelarCita(@PathVariable("id") Long id) {
        String resultado = citaMedicaService.cancelarCita(id);
        return ResponseEntity.ok(resultado);
    }

    // Corregido: Se agregó el parámetro Throwable t para interceptar la excepción correctamente
    public ResponseEntity<CitaMedica> crearCitaFallback(CitaMedica cita, Throwable t) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> cancelarCitaFallback(Long id, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("No se pudo procesar la cancelación de la cita " + id + " en este momento. Intente más tarde.");
    }
}