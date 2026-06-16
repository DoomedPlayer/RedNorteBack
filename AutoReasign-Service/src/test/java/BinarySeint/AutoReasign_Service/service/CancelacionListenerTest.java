package BinarySeint.AutoReasign_Service.service;

import BinarySeint.AutoReasign_Service.cliente.WaitlistClient;
import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.Reasignacion;
import BinarySeint.AutoReasign_Service.repository.ReasignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelacionListenerTest {

    @Mock
    private ReasignacionRepository repository;

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
    void testProcesarCancelacion_ConPacienteEnEspera_GuardaReasignacion() {
        String rutSiguientePaciente = "98765432-1";
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(rutSiguientePaciente);

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(repository, times(1)).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_SinPacienteEnEspera_NoGuardaNada() {

        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(null);

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(repository, never()).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_ManejaExcepcionDelClienteFeign() {

        when(waitlistClient.obtenerSiguientePaciente("Dermatologia"))
                .thenThrow(new RuntimeException("Error de conexión"));

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(repository, never()).save(any(Reasignacion.class));
    }
}