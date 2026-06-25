package BinarySeint.BFF.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoBffControllerTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private MedicoBffController controller;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);
        controller = new MedicoBffController(webClientBuilder);
    }

    @Test
    void testObtenerListaEspera_RetornaLista() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        Object respuestaDummy = "[{paciente: 1}]";
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(respuestaDummy));

        Object resultado = controller.obtenerListaEspera(5).block();

        assertNotNull(resultado);
        assertEquals(respuestaDummy, resultado);
        verify(requestHeadersUriSpec, times(1)).uri("http://waitlist-service:8081/api/espera/lista/5");
    }

    @Test
    void testCrearCita_ProxyExitoso() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        Object citaData = "datos_nueva_cita";
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just("Cita Creada"));

        Object resultado = controller.crearCita(citaData).block();

        assertEquals("Cita Creada", resultado);
        verify(requestBodyUriSpec, times(1)).uri("http://auto-reasign-service:8083/api/citas");
    }

    @Test
    void testReasignarCita_ProxyExitoso() {
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just("Cita Actualizada"));

        Object resultado = controller.reasignarCita(10L, "datos_actualizados").block();

        assertEquals("Cita Actualizada", resultado);
        verify(requestBodyUriSpec, times(1)).uri("http://auto-reasign-service:8083/api/citas/10");
    }

    @Test
    void testEliminarCita_ProxyExitoso() {
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        Void resultado = controller.eliminarCita(10L).block();

        assertNull(resultado);
        verify(requestHeadersUriSpec, times(1)).uri("http://auto-reasign-service:8083/api/citas/10");
    }

    @Test
    void testCancelarCita_ProxyExitoso() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Cita Cancelada y Reasignada"));

        String resultado = controller.cancelarCita(10L).block();

        assertEquals("Cita Cancelada y Reasignada", resultado);
        verify(requestBodyUriSpec, times(1)).uri("http://auto-reasign-service:8083/api/citas/10/cancelar");
    }
}