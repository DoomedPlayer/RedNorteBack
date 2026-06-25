package BinarySeint.AutoReasign_Service.controller;

import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.service.CitaMedicaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@Tag(name = "Citas Médicas", description = "Endpoints para la gestión, reasignación y cancelación de citas")
public class CitaMedicaController {

    @Autowired
    private CitaMedicaService citaMedicaService;

    @PostMapping
    @CircuitBreaker(name = "citaMedicaCB", fallbackMethod = "crearCitaFallback")
    @Operation(summary = "Crear nueva cita", description = "Registra una cita médica y le asigna el estado 'ACTIVA' por defecto.")
    @ApiResponse(responseCode = "201", description = "Cita creada")
    public ResponseEntity<CitaMedica> crearCita(@RequestBody CitaMedica cita) {
        CitaMedica nuevaCita = citaMedicaService.crearCita(cita);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las citas", description = "Retorna el listado global de citas médicas.")
    public ResponseEntity<List<CitaMedica>> obtenerTodas() {
        List<CitaMedica> citas = citaMedicaService.obtenerTodasLasCitas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/{rut}")
    @Operation(summary = "Citas por paciente", description = "Filtra las citas asociadas a un RUT específico.")
    public ResponseEntity<List<CitaMedica>> obtenerCitasPorPaciente(@PathVariable("rut") String rut) {
        List<CitaMedica> citas = citaMedicaService.obtenerCitasPorRutPaciente(rut);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Cita por ID", description = "Retorna los detalles exactos de una cita específica.")
    @ApiResponse(responseCode = "200", description = "Cita encontrada")
    @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    public ResponseEntity<CitaMedica> obtenerPorId(@PathVariable("id") Long id) {
        CitaMedica cita = citaMedicaService.obtenerCitaPorId(id);
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/especialidad/{nombre}")
    @Operation(summary = "Citas por especialidad", description = "Filtra las citas agendadas para una especialidad clínica.")
    public ResponseEntity<List<CitaMedica>> obtenerCitasPorEspecialidad(@PathVariable("nombre") String nombre) {
        List<CitaMedica> citas = citaMedicaService.obtenerCitasPorEspecialidad(nombre);
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita", description = "Sobrescribe los datos de una cita existente (ej. reasignación de hora o médico).")
    public ResponseEntity<CitaMedica> actualizarCita(@PathVariable("id") Long id, @RequestBody CitaMedica datosActualizados) {
        CitaMedica citaEditada = citaMedicaService.actualizarCita(id, datosActualizados);
        return ResponseEntity.ok(citaEditada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cita", description = "Borra físicamente el registro de la cita de la base de datos.")
    @ApiResponse(responseCode = "204", description = "Cita eliminada")
    public ResponseEntity<Void> eliminarCita(@PathVariable("id") Long id) {
        citaMedicaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    @CircuitBreaker(name = "citaMedicaCB", fallbackMethod = "cancelarCitaFallback")
    @Operation(summary = "Cancelar y notificar", description = "Marca la cita como CANCELADA y emite un evento a RabbitMQ para iniciar la reasignación.")
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