package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.EstadoPaciente;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import BinarySeint.Waitlist_Service.service.WaitlistService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/espera")
@Tag(name = "Lista de Espera", description = "Gestión de pacientes en espera por especialidad y prioridad")
public class WaitlistController {

    @Autowired
    private WaitlistService service;

    @PostMapping("/paciente")
    @Operation(summary = "Crear registro de paciente", description = "Crea o actualiza el estado de espera de un paciente.")
    @ApiResponse(responseCode = "201", description = "Registro creado exitosamente")
    public ResponseEntity<RegistroPaciente> crearRegistroPaciente(@RequestBody Map<String, Object> request) {
        String rut = request.get("rutPaciente").toString();

        String estadoStr = request.get("estado") != null ? request.get("estado").toString() : "SIN_REGISTROS";
        EstadoPaciente estado = EstadoPaciente.valueOf(estadoStr);
        
        String prioridad = request.get("prioridad") != null ? request.get("prioridad").toString() : "Sin prioridad";
        
        RegistroPaciente registrado = service.guardarRegistroPaciente(rut, estado, prioridad);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/paciente/{rut}")
    @Operation(summary = "Obtener estado de espera", description = "Consulta la situación actual de un paciente específico mediante su RUT.")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado en los registros")
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
    @Operation(summary = "Modificar estado (Doctor)", description = "Permite a un médico actualizar el estado y prioridad de un paciente.")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "400", description = "Datos de estado inválidos")
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
    @Operation(summary = "Ingresar a lista de espera", description = "Añade un paciente a la fila de una especialidad médica y genera su ticket.")
    @ApiResponse(responseCode = "201", description = "Paciente ingresado a la lista")
    @CircuitBreaker(name = "waitlistCB", fallbackMethod = "registrarListaFallback")
    public ResponseEntity<RegistroPaciente> registrarEnListaEspera(@RequestBody Map<String, Object> request) {
        Integer idEspecialidad = Integer.parseInt(request.get("idEspecialidad").toString());
        
        // Crea el ticket de lista de espera y sincroniza el RegistroPaciente a "EN_ESPERA"
        RegistroPaciente registrado = service.registrarEnListaEspera(
                request.get("rutPaciente").toString(),
                idEspecialidad,
                request.get("tipoAtencion").toString()
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @GetMapping("/lista/{idEspecialidad}")
    @Operation(summary = "Ver fila por especialidad", description = "Obtiene la lista de espera ordenada por prioridad y fecha de ingreso.")
    public ResponseEntity<List<ListaEspera>> obtenerLista(@PathVariable("idEspecialidad") Integer idEspecialidad) {
        List<ListaEspera> lista = service.obtenerListaPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/lista/{rut}")
    @Operation(summary = "Eliminar de la lista", description = "Saca al paciente de la fila y actualiza su estado a HORA_ASIGNADA.")
    @ApiResponse(responseCode = "204", description = "Paciente removido de la lista")
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
    @Operation(summary = "Obtener siguiente paciente", description = "Extrae y remueve de la lista al paciente con mayor prioridad para una especialidad.")
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