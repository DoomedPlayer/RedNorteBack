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
import BinarySeint.BFF.dto.RegistroPacienteBFF;
import BinarySeint.BFF.dto.ListaEsperaDTO;
import BinarySeint.BFF.dto.PacienteDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff") 
@PreAuthorize("hasAnyAuthority('PACIENTE', 'ROLE_PACIENTE')") 
@Tag(name = "BFF Dashboard Paciente", description = "Orquestador de vistas frontend para la interfaz principal del paciente")
public class PacienteDashboardController {

    private final WebClient webClient;

    public PacienteDashboardController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/dashboard")
    @CircuitBreaker(name = "dashboardCB", fallbackMethod = "getDashboardCompletoFallback")
    @Operation(summary = "Cargar Dashboard Completo", description = "Ejecuta peticiones asíncronas a Patient Portal, Waitlist y Citas para consolidar la vista inicial.")
    @ApiResponse(responseCode = "200", description = "Dashboard consolidado y renderizado")
    public Mono<DashboardDTO> getDashboardCompleto(Authentication authentication, HttpServletRequest request) { 

        String rutId = authentication.getName();
        String token = request.getHeader("Authorization"); 

        Mono<PacienteDTO> pacienteMono = webClient
                .get()
                .uri("http://patient-portal:8084/api/portal/pacientes/" + rutId)
                .header("Authorization", token) 
                .retrieve()
                .bodyToMono(PacienteDTO.class)
                .doOnError(e -> System.err.println("❌ ERROR DESDE PATIENT-PORTAL: " + e.getMessage()))
                .onErrorReturn(PacienteDTO.builder()
                        .rut(rutId)
                        .nombreCompleto("Paciente (Datos no disponibles)")
                        .correo("-")
                        .build());
                
        Mono<ListaEsperaDTO> esperaMono = webClient
                .get()
                .uri("http://waitlist-service:8081/api/espera/paciente/" + rutId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(RegistroPacienteBFF.class)
                .map(registroRaw -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String fechaFormateada = registroRaw.getFechaRegistro() != null 
                            ? registroRaw.getFechaRegistro().format(formatter) 
                            : "-";

                    String estadoFrontend = "Sin información";
                    if (registroRaw.getEstado() != null) {
                        switch (registroRaw.getEstado()) {
                            case "EN_ESPERA":
                                estadoFrontend = "Pendiente de asignación médica";
                                break;
                            case "HORA_ASIGNADA":
                                estadoFrontend = "Hora médica asignada";
                                break;
                            case "EVALUANDO_ANTECEDENTES":
                                estadoFrontend = "En evaluación clínica";
                                break;
                            case "SIN_REGISTROS":
                                estadoFrontend = "Sin registros activos";
                                break;
                            default:
                                estadoFrontend = registroRaw.getEstado();
                        }
                    }

                    String textoPrioridad = registroRaw.getPrioridad() != null ? registroRaw.getPrioridad() : "-";

                    return new ListaEsperaDTO(estadoFrontend, fechaFormateada, textoPrioridad, registroRaw.isGesAuge());
                })
                .onErrorReturn(new ListaEsperaDTO("Sin registros", "-", "-", false));
                
        Mono<List<CitaMedicaDTO>> citaMono = webClient
                .get()
                .uri("http://auto-reasign-service:8083/api/citas/paciente/"+ rutId)
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(CitaMedicaDTO.class) 
                .collectList()                  
                .onErrorReturn(new ArrayList<>());
                
        Mono<List<DocumentoDTO>> documentoMono = webClient
                .get()
                .uri("http://patient-portal:8084/api/portal/pacientes/" + rutId + "/documentos")
                .header("Authorization", token)
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

    public Mono<DashboardDTO> getDashboardCompletoFallback(Authentication authentication, HttpServletRequest request, Throwable t) {

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