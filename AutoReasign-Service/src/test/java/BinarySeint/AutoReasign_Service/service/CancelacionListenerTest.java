package BinarySeint.AutoReasign_Service.service;

import BinarySeint.AutoReasign_Service.cliente.WaitlistClient;
import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.model.Reasignacion;
import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import BinarySeint.AutoReasign_Service.repository.ReasignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CancelacionListenerTest {

    @Mock
    private ReasignacionRepository reasignacionRepo;

    @Mock
    private CitaMedicaRepository citaRepository;

    @Mock
    private WaitlistClient waitlistClient;

    @InjectMocks
    private CancelacionListener cancelacionListener;

    private EventoCancelacionDTO eventoDummy;

    @BeforeEach
    void setUp() {
        eventoDummy = new EventoCancelacionDTO();
        eventoDummy.setIdCitaOriginal(100L);
        eventoDummy.setEspecialidad("Dermatologia");
        eventoDummy.setRutPacienteCancelado("12345678-9");
    }

    @Test
    void testProcesarCancelacion_ConPacienteEnEsperaYCitaExiste_GuardaReasignacion() {
        String rutSiguientePaciente = "98765432-1";
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(rutSiguientePaciente);

        CitaMedica citaOriginalDummy = new CitaMedica();
        citaOriginalDummy.setId(100L);
        citaOriginalDummy.setEspecialidad("Dermatologia");
        citaOriginalDummy.setFechaHora(LocalDateTime.now().plusDays(5));
        
        when(citaRepository.findById(100L)).thenReturn(Optional.of(citaOriginalDummy));

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(citaRepository, times(1)).save(any(CitaMedica.class));
        verify(reasignacionRepo, times(1)).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_CitaOriginalNoEncontrada_NoGuardaNada() {
        String rutSiguientePaciente = "98765432-1";
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(rutSiguientePaciente);

        when(citaRepository.findById(100L)).thenReturn(Optional.empty());

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(citaRepository, times(1)).findById(100L);
        verify(citaRepository, never()).save(any(CitaMedica.class));
        verify(reasignacionRepo, never()).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_SinPacienteEnEsperaNull_NoGuardaNada() {
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(null);

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(citaRepository, never()).findById(anyLong());
        verify(citaRepository, never()).save(any(CitaMedica.class)); 
        verify(reasignacionRepo, never()).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_SinPacienteEnEsperaVacio_NoGuardaNada() {
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn("");

        cancelacionListener.procesarCancelacion(eventoDummy);

        // Comprobamos la condición !rutReal.isEmpty() del listener
        verify(citaRepository, never()).findById(anyLong());
        verify(citaRepository, never()).save(any(CitaMedica.class)); 
        verify(reasignacionRepo, never()).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_ManejaExcepcion_NoRompeAplicacion() {
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia"))
                .thenThrow(new RuntimeException("Error simulado de conexión con Waitlist Service"));

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(citaRepository, never()).findById(anyLong());
        verify(reasignacionRepo, never()).save(any(Reasignacion.class));
    }
}