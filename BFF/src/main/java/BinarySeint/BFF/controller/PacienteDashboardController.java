package BinarySeint.BFF.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import BinarySeint.BFF.dto.CitaMedicaDTO;
import BinarySeint.BFF.dto.DashboardDTO;
import BinarySeint.BFF.dto.DocumentoDTO;
import BinarySeint.BFF.dto.RegistroEsperaBFF;
import BinarySeint.BFF.dto.ListaEsperaDTO;
import BinarySeint.BFF.dto.PacienteDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bff") 
@PreAuthorize("hasRole('PACIENTE')")
public class PacienteDashboardController {

    private final WebClient webClient;

    public PacienteDashboardController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/dashboard")
    @CircuitBreaker(name = "dashboardCB", fallbackMethod = "getDashboardCompletoFallback")
    public Mono<DashboardDTO> getDashboardCompleto(Authentication authentication) { 

        String rutId = authentication.getName();

        Mono<PacienteDTO> pacienteMono = webClient
                .get()
                .uri("http://patient-portal:8084/api/portal/patients/" + rutId)
                .retrieve()
                .bodyToMono(PacienteDTO.class);
                
        Mono<ListaEsperaDTO> esperaMono = webClient
                .get()
                .uri("http://waitlist-service:8081/api/espera/paciente/" + rutId)
                .retrieve()
                .bodyToMono(RegistroEsperaBFF.class)
                .map(registroRaw -> {
                    // 1. Formatear la fecha
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String fechaFormateada = registroRaw.getFechaIngreso() != null ? registroRaw.getFechaIngreso().format(formatter) : "-";

                    // 2. Traducir prioridad
                    String textoPrioridad = "Normal";
                    if (registroRaw.getNivelPrioridad() != null) {
                        if (registroRaw.getNivelPrioridad() == 1) textoPrioridad = "Alta (Urgencia)";
                        else if (registroRaw.getNivelPrioridad() <= 3) textoPrioridad = "Media - Alta";
                    }

                    // 3. Traducir estado
                    String estadoFrontend = "En espera".equalsIgnoreCase(registroRaw.getEstado()) 
                            ? "Pendiente de asignación médica" 
                            : registroRaw.getEstado();

                    // 4. Retornar el DTO final
                    return new ListaEsperaDTO(estadoFrontend, fechaFormateada, textoPrioridad, registroRaw.isGesAuge());
                })
                .onErrorReturn(new ListaEsperaDTO("Sin registros", "-", "-", false));
                
        Mono<List<CitaMedicaDTO>> citaMono = webClient
                .get()
                .uri("http://auto-reasign_service:8083/api/citas/paciente/"+ rutId)
                .retrieve()
                .bodyToFlux(CitaMedicaDTO.class) 
                .collectList()                  
                .onErrorReturn(new ArrayList<>());
                
        Mono<List<DocumentoDTO>> documentoMono = webClient
                .get()
                .uri("http://patient-portal:8084/api/v1/portal/pacientes/" + rutId + "/documentos")
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

    public Mono<DashboardDTO> getDashboardCompletoFallback(Authentication authentication, Throwable t) {

        String rutId = (authentication != null) ? authentication.getName() : "Desconocido";

        PacienteDTO pacienteError = PacienteDTO.builder()
                .rut(rutId)
                .nombreCompleto("Servicio no disponible")
                .correo("-")
                .estadoListaEspera("Error de conexión temporal")
                .build();
        return Mono.just(new DashboardDTO(pacienteError, null, new ArrayList<>(), new ArrayList<>()));
    }   
}