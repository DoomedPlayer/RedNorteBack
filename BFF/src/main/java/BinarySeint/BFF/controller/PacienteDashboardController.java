package BinarySeint.BFF.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.rednorte.portal.dtos.CitaMedicaDTO;
import com.rednorte.portal.dtos.DocumentoDTO;
import com.rednorte.portal.dtos.PacienteDTO;

import BinarySeint.BFF.dto.DashboardDTO;
import BinarySeint.Waitlist_Service.dto.ListaEsperaDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/bff")
public class PacienteDashboardController {

    private final WebClient webClient;

    public PacienteDashboardController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/dashboard/{id}")
    @CircuitBreaker(name = "dashboardCB", fallbackMethod = "getDashboardCompletoFallback")
    public Mono<DashboardDTO> getDashboardCompleto(@PathVariable("id") String id) { 

        Mono<PacienteDTO> pacienteMono = webClient
                .get()
                .uri("http://localhost:8081/api/patients/" + id)
                .retrieve()
                .bodyToMono(PacienteDTO.class);
                
        Mono<ListaEsperaDTO> esperaMono = webClient
                .get()
                .uri("http://localhost:8082/api/waitlist/paciente/" + id)
                .retrieve()
                .bodyToMono(ListaEsperaDTO.class);
                
        Mono<List<CitaMedicaDTO>> citaMono = webClient
                .get()
                .uri("http://localhost:8081/api/v1/portal/pacientes/" + id + "/citas")
                .retrieve()
                .bodyToFlux(CitaMedicaDTO.class) 
                .collectList()                  
                .onErrorReturn(new ArrayList<>());
                
        Mono<List<DocumentoDTO>> documentoMono = webClient
                .get()
                .uri("http://localhost:8081/api/v1/portal/pacientes/" + id + "/documentos")
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
