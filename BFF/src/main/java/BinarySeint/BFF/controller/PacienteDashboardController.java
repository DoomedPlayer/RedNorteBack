package BinarySeint.BFF.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.rednorte.portal.dtos.CitaMedicaDTO;
import com.rednorte.portal.dtos.DocumentoDTO;
import com.rednorte.portal.dtos.PacienteDTO;

import BinarySeint.BFF.dto.DashboardDTO;
import BinarySeint.Waitlist_Service.dto.ListaEsperaDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bff")
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteDashboardController {

    private final WebClient.Builder webClientBuilder;

    public PacienteDashboardController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    @GetMapping("/dashboard/{id}")
    @CircuitBreaker(name = "dashboardCB", fallbackMethod = "getDashboardCompletoFallback")
    public Mono<DashboardDTO> getDashboardCompleto(@PathVariable String id) {

        Mono<PacienteDTO> pacienteMono = webClientBuilder.build()
                .get()
                .uri("http://patient-portal:8084/api/patients/" + id)
                .retrieve()
                .bodyToMono(PacienteDTO.class);

        Mono<ListaEsperaDTO> esperaMono = webClientBuilder.build()
                .get()
                .uri("http://waitlist-service:8081/api/espera/lista/" + id)
                .retrieve()
                .bodyToMono(ListaEsperaDTO.class)
                .onErrorReturn(new ListaEsperaDTO());

        Mono<List<CitaMedicaDTO>> citaMono = webClientBuilder.build()
                .get()
                .uri("http://patient-portal:8084/api/v1/portal/pacientes/" + id + "/citas")
                .retrieve()
                .bodyToFlux(CitaMedicaDTO.class) 
                .collectList()                  
                .onErrorReturn(new ArrayList<>());

        Mono<List<DocumentoDTO>> documentoMono = webClientBuilder.build()
                .get()
                .uri("http://patient-portal:8084/api/v1/portal/pacientes/" + id + "/documentos")
                .retrieve()
                .bodyToFlux(DocumentoDTO.class) 
                .collectList()                  
                .onErrorReturn(new ArrayList<>());

        return Mono.zip(pacienteMono, esperaMono, citaMono, documentoMono)
                .map(tuple -> {
                    PacienteDTO paciente = tuple.getT1();
                    ListaEsperaDTO listaEspera = tuple.getT2();
                    List<CitaMedicaDTO> citas = tuple.getT3();
                    List<DocumentoDTO> documentos = tuple.getT4();
                    
                    return new DashboardDTO(paciente, listaEspera, citas, documentos);
                });
    }

    @PostMapping("/paciente/agendar")
    public Mono<Object> agendarHora(@RequestBody Object datosRegistro) {
        return webClientBuilder.build()
                .post()
                .uri("http://waitlist-service:8081/api/espera/registrar")
                .bodyValue(datosRegistro)
                .retrieve()
                .bodyToMono(Object.class);
    }


    @PostMapping("/paciente/anular/{idCita}")
    public Mono<String> anularHora(@PathVariable Long idCita) {
        return webClientBuilder.build()
                .post()
                .uri("http://auto-reasign-service:8083/api/citas/" + idCita + "/cancelar")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<DashboardDTO> getDashboardCompletoFallback(String id, Throwable t) {
        PacienteDTO pacienteError = PacienteDTO.builder()
                .rut(id)
                .nombreCompleto("Servicio no disponible")
                .correo("-")
                .estadoListaEspera("Error de conexión temporal")
                .build();
        return Mono.just(new DashboardDTO(pacienteError, null, new ArrayList<>(), new ArrayList<>()));
    }    
}