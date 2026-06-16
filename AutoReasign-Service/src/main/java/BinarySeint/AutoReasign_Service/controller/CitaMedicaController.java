package BinarySeint.AutoReasign_Service.controller;

import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.service.CitaMedicaService;
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

    // C - CREAR
    @PostMapping
    public ResponseEntity<CitaMedica> crearCita(@RequestBody CitaMedica cita) {
        CitaMedica nuevaCita = citaMedicaService.crearCita(cita);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CitaMedica>> obtenerTodas() {
        List<CitaMedica> citas = citaMedicaService.obtenerTodasLasCitas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> obtenerPorId(@PathVariable Long id) {
        CitaMedica cita = citaMedicaService.obtenerCitaPorId(id);
        return ResponseEntity.ok(cita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> actualizarCita(@PathVariable Long id, @RequestBody CitaMedica datosActualizados) {
        CitaMedica citaEditada = citaMedicaService.actualizarCita(id, datosActualizados);
        return ResponseEntity.ok(citaEditada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaMedicaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarCita(@PathVariable Long id) {
        String resultado = citaMedicaService.cancelarCita(id);
        return ResponseEntity.ok(resultado);
    }
}