package BinarySeint.BFF.controller;

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
import BinarySeint.Waitlist_Service.model.RegistroEspera;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/bff")
public class PacienteDashboardController {

    private final WebClient.Builder webClientBuilder;

    public PacienteDashboardController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/dashboard/{id}")
    public Mono<DashboardDTO> getDashboardCompleto(@PathVariable String id) {
        Mono<PacienteDTO> pacienteMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/api/patients/" + id)
                        .retrieve()
                        .bodyToMono(PacienteDTO.class);

        // 2. Llamada interna al microservicio de lista de espera
        Mono<ListaEsperaDTO> esperaMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/waitlist/paciente/" + id)
                .retrieve()
                .bodyToMono(ListaEsperaDTO.class);
        Mono<List<CitaMedicaDTO>> citaMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/waitlist/paciente/" + id)
                .retrieve()
                .bodyToMono(List<CitaMedicaDTO>.class);
        Mono<List<DocumentoDTO>> documentoMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/waitlist/paciente/" + id)
                .retrieve()
                .bodyToMono(List<DocumentoDTO>.class);


        // 3. Combinamos ambas respuestas en un solo objeto optimizado para React
        return Mono.zip(pacienteMono, esperaMono, citaMono, documentoMono)
                .map(tuple -> new DashboardDTO(tuple.getT1(), tuple.getT2(),tuple.getT3(),tuple.getT4()));
    }
        
}
