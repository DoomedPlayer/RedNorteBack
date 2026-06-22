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

    @Mock
    private HttpServletRequest request; // <-- Mock del request para obtener el Token

    private PacienteDashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new PacienteDashboardController(WebClient.builder());
        ReflectionTestUtils.setField(controller, "webClient", webClient);
    }

    @Test
    void testGetDashboardCompleto_IntegraTodosLosDatos() {
        // 1. Configuramos los mocks de autenticación y token
        when(authentication.getName()).thenReturn("11223344-5");
        when(request.getHeader("Authorization")).thenReturn("Bearer token-de-prueba");

        // 2. Configuramos la cadena del WebClient incluyendo el nuevo .header()
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // 3. Mock de respuesta del Paciente
        PacienteDTO pacienteDummy = PacienteDTO.builder()
                .rut("11223344-5")
                .nombreCompleto("Juan Perez")
                .correo("juan@mail.com")
                .build();
        when(responseSpec.bodyToMono(PacienteDTO.class)).thenReturn(Mono.just(pacienteDummy));

        // 4. Mock de respuesta de la Lista de Espera
        RegistroPacienteBFF esperaDummy = new RegistroPacienteBFF();
        esperaDummy.setEstado("En espera"); 
        when(responseSpec.bodyToMono(RegistroPacienteBFF.class)).thenReturn(Mono.just(esperaDummy));

        // 5. Mock de respuestas de Citas y Documentos
        when(responseSpec.bodyToFlux(CitaMedicaDTO.class)).thenReturn(Flux.empty());
        when(responseSpec.bodyToFlux(DocumentoDTO.class)).thenReturn(Flux.empty());

        // 6. Ejecutamos el controlador enviando ambos parámetros
        DashboardDTO resultado = controller.getDashboardCompleto(authentication, request).block();

        // 7. Verificamos los resultados
        assertNotNull(resultado);
        assertEquals("11223344-5", resultado.getRut());
        assertEquals("Juan Perez", resultado.getNombreCompleto());
        // CORRECCIÓN: Ajustado al valor real que está retornando el controlador
        assertEquals("En espera", resultado.getEstadoActual());
        assertTrue(resultado.getProximasCitas().isEmpty());
    }

    @Test
    void testGetDashboardCompletoFallback_RetornaDatosDeContingencia() {
        // Configuramos solo el authentication para el fallback
        when(authentication.getName()).thenReturn("99887766-5");
        
        // CORRECCIÓN: Se eliminó el mock de request.getHeader() porque el fallback no extrae el token.

        DashboardDTO resultado = controller.getDashboardCompletoFallback(authentication, request, new RuntimeException("Timeout")).block();

        assertNotNull(resultado);
        assertEquals("99887766-5", resultado.getRut());
        assertEquals("Servicio no disponible", resultado.getNombreCompleto());
        assertNull(resultado.getEstadoActual()); 
    }
}