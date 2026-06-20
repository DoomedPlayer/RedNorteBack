package BinarySeint.BFF.controller;

import BinarySeint.BFF.dto.CitaMedicaDTO;
import BinarySeint.BFF.dto.DashboardDTO;
import BinarySeint.BFF.dto.DocumentoDTO;
import BinarySeint.BFF.dto.ListaEsperaDTO;
import BinarySeint.BFF.dto.PacienteDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication; // <-- Nueva importación requerida
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteDashboardControllerTest {

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

    private PacienteDashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new PacienteDashboardController(WebClient.builder());
        ReflectionTestUtils.setField(controller, "webClient", webClient);
    }

    @Test
    void testGetDashboardCompleto_IntegraTodosLosDatos() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        PacienteDTO pacienteDummy = PacienteDTO.builder()
                .rut("11223344-5")
                .nombreCompleto("Juan Perez")
                .correo("juan@mail.com")
                .build();
        when(responseSpec.bodyToMono(PacienteDTO.class)).thenReturn(Mono.just(pacienteDummy));

        ListaEsperaDTO esperaDummy = new ListaEsperaDTO();
        esperaDummy.setEstado("EN_ESPERA");
        when(responseSpec.bodyToMono(ListaEsperaDTO.class)).thenReturn(Mono.just(esperaDummy));

        when(responseSpec.bodyToFlux(CitaMedicaDTO.class)).thenReturn(Flux.empty());
        when(responseSpec.bodyToFlux(DocumentoDTO.class)).thenReturn(Flux.empty());

        when(authentication.getName()).thenReturn("11223344-5");

        DashboardDTO resultado = controller.getDashboardCompleto(authentication).block();

        assertNotNull(resultado);
        assertEquals("11223344-5", resultado.getRut());
        assertEquals("Juan Perez", resultado.getNombreCompleto());
        assertEquals("EN_ESPERA", resultado.getEstadoActual());
        assertTrue(resultado.getProximasCitas().isEmpty());
    }

    @Test
    void testGetDashboardCompletoFallback_RetornaDatosDeContingencia() {

        when(authentication.getName()).thenReturn("99887766-5");

        DashboardDTO resultado = controller.getDashboardCompletoFallback(authentication, new RuntimeException("Timeout")).block();

        assertNotNull(resultado);
        assertEquals("99887766-5", resultado.getRut());
        assertEquals("Servicio no disponible", resultado.getNombreCompleto());
        assertNull(resultado.getEstadoActual()); 
    }
}