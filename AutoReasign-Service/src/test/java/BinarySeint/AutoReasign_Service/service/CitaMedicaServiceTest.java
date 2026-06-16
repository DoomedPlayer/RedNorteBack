package BinarySeint.AutoReasign_Service.service;

import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository citaRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CitaMedicaService citaMedicaService;

    private CitaMedica citaDummy;

    @BeforeEach
    void setUp() {
        citaDummy = new CitaMedica();
        citaDummy.setId(1L);
        citaDummy.setRutPaciente("12345678-9");
        citaDummy.setEspecialidadYTipo("MedicinaGeneral");
        citaDummy.setFechaHora(LocalDateTime.now().plusDays(2));
        citaDummy.setEstado("ACTIVA");
    }

    @Test
    void testCrearCita_AsignaEstadoActivaPorDefecto() {
        CitaMedica nuevaCita = new CitaMedica();
        nuevaCita.setRutPaciente("98765432-1");
        when(citaRepository.save(any(CitaMedica.class))).thenAnswer(invocation -> {
            CitaMedica c = invocation.getArgument(0);
            c.setId(2L);
            return c;
        });
        CitaMedica resultado = citaMedicaService.crearCita(nuevaCita); //

        assertEquals("ACTIVA", resultado.getEstado());
        assertNotNull(resultado.getId());
        verify(citaRepository, times(1)).save(any(CitaMedica.class));
    }

    @Test
    void testCancelarCita_CambiaEstadoYEnviaEvento() {

        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaDummy));
        when(citaRepository.save(any(CitaMedica.class))).thenReturn(citaDummy);

        String resultado = citaMedicaService.cancelarCita(1L); //

        assertEquals("CANCELADA", citaDummy.getEstado());
        assertTrue(resultado.contains("ha sido CANCELADA"));

        verify(citaRepository, times(1)).save(citaDummy);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("citas.canceladas.queue"), 
                any(EventoCancelacionDTO.class)
        );
    }

    @Test
    void testObtenerCitaPorId_LanzaExcepcionSiNoExiste() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            citaMedicaService.obtenerCitaPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Cita no encontrada con el ID: 99"));
    }
}