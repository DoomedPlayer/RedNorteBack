package BinarySeint.BFF.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/medico")
@PreAuthorize("hasRole('DOCTOR')")
@CrossOrigin(origins = "*")
public class MedicoBffController {

    private final WebClient.Builder webClientBuilder;

    public MedicoBffController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // =========================================================================
    // R - LEER: Cargar lista de espera por especialidad (Para la grilla médica)
    // =========================================================================
    @GetMapping("/espera/lista/{idEspecialidad}")
    public Mono<Object> obtenerListaEspera(@PathVariable Integer idEspecialidad) {
        return webClientBuilder.build()
                .get()
                .uri("http://waitlist-service:8081/api/espera/lista/" + idEspecialidad)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // =========================================================================
    // C - CREAR: Dar u otorgar una nueva Cita Médica
    // =========================================================================
    @PostMapping("/citas")
    public Mono<Object> crearCita(@RequestBody Object citaData) {
        return webClientBuilder.build()
                .post()
                .uri("http://auto-reasign-service:8083/api/citas")
                .bodyValue(citaData)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // =========================================================================
    // U - ACTUALIZAR: Reasignar / Editar datos de la cita médica por ID
    // =========================================================================
    @PutMapping("/citas/{id}")
    public Mono<Object> reasignarCita(@PathVariable Long id, @RequestBody Object datosActualizados) {
        return webClientBuilder.build()
                .put()
                .uri("http://auto-reasign-service:8083/api/citas/" + id)
                .bodyValue(datosActualizados)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // =========================================================================
    // D - ELIMINAR / BORRAR: Quitar cita médica por completo
    // =========================================================================
    @DeleteMapping("/citas/{id}")
    public Mono<Void> eliminarCita(@PathVariable Long id) {
        return webClientBuilder.build()
                .delete()
                .uri("http://auto-reasign-service:8083/api/citas/" + id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // =========================================================================
    // POST: Flujo específico de Cancelación con lógica/estado
    // =========================================================================
    @PostMapping("/citas/{id}/cancelar")
    public Mono<String> cancelarCita(@PathVariable Long id) {
        return webClientBuilder.build()
                .post()
                .uri("http://auto-reasign-service:8083/api/citas/" + id + "/cancelar")
                .retrieve()
                .bodyToMono(String.class);
    }
}   