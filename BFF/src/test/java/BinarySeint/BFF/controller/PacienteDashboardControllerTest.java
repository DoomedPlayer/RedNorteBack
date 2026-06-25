package BinarySeint.BFF.controller;

import BinarySeint.BFF.dto.CitaMedicaDTO;
import BinarySeint.BFF.dto.DashboardDTO;
import BinarySeint.BFF.dto.DocumentoDTO;
import BinarySeint.BFF.dto.ListaEsperaDTO;
import BinarySeint.BFF.dto.PacienteDTO;
import BinarySeint.BFF.dto.RegistroPacienteBFF; // <-- Importación requerida para el nuevo flujo

import jakarta.servlet.http.HttpServletRequest; // <-- Importación para mockear la petición web

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication; 
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteDashboardControllerTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    private PacienteDashboardController controller;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);
        controller = new PacienteDashboardController(webClientBuilder);
    }

    @Test
    void testGetDashboardCompleto_IntegraTodosLosDatosYMapeaEstado() {
        when(authentication.getName()).thenReturn("11223344-5");
        when(request.getHeader("Authorization")).thenReturn("Bearer token-de-prueba");

        // Simulación de la cadena WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        PacienteDTO pacienteDummy = PacienteDTO.builder()
                .rut("11223344-5")
                .nombreCompleto("Juan Perez")
                .build();
        when(responseSpec.bodyToMono(PacienteDTO.class)).thenReturn(Mono.just(pacienteDummy));

        RegistroPacienteBFF esperaDummy = new RegistroPacienteBFF();
        esperaDummy.setEstado("EN_ESPERA"); 
        esperaDummy.setFechaRegistro(LocalDate.of(2026, 6, 25));
        esperaDummy.setPrioridad("Nivel 2");
        when(responseSpec.bodyToMono(RegistroPacienteBFF.class)).thenReturn(Mono.just(esperaDummy));

        // Mock Citas y Documentos vacíos
        when(responseSpec.bodyToFlux(CitaMedicaDTO.class)).thenReturn(Flux.empty());
        when(responseSpec.bodyToFlux(DocumentoDTO.class)).thenReturn(Flux.empty());

        DashboardDTO resultado = controller.getDashboardCompleto(authentication, request).block();

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombreCompleto());
        assertEquals("Pendiente de asignación médica", resultado.getEstadoActual());
        assertEquals("25-06-2026", resultado.getFechaIngresoLista());
    }

    @Test
    void testGetDashboardCompletoFallback_RetornaDatosDeContingencia() {
        when(authentication.getName()).thenReturn("99887766-5");

        DashboardDTO resultado = controller.getDashboardCompletoFallback(authentication, request, new RuntimeException("Timeout")).block();

        assertNotNull(resultado);
        assertEquals("99887766-5", resultado.getRut());
        assertEquals("Servicio no disponible", resultado.getNombreCompleto());

        assertNull(resultado.getEstadoActual());
    }
}