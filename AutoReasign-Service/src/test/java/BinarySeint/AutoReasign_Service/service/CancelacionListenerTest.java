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
    private ReasignacionRepository repository;

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
    void testProcesarCancelacion_ConPacienteEnEspera_GuardaReasignacion() {
        String rutSiguientePaciente = "98765432-1";
        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(rutSiguientePaciente);

        // 2. AÑADIDO: Simulamos que la cita original SÍ existe en la base de datos
        CitaMedica citaOriginalDummy = new CitaMedica();
        citaOriginalDummy.setId(100L);
        citaOriginalDummy.setEspecialidad("Dermatologia");
        citaOriginalDummy.setFechaHora(LocalDateTime.now().plusDays(5));
        
        when(citaRepository.findById(100L)).thenReturn(Optional.of(citaOriginalDummy));

        // Ejecutamos el método
        cancelacionListener.procesarCancelacion(eventoDummy);

        // 3. Verificamos que ahora sí se guardan la nueva cita y la reasignación
        verify(citaRepository, times(1)).save(any(CitaMedica.class));
        verify(repository, times(1)).save(any(Reasignacion.class));
    }

    @Test
    void testProcesarCancelacion_SinPacienteEnEspera_NoGuardaNada() {

        when(waitlistClient.obtenerSiguientePaciente("Dermatologia")).thenReturn(null);

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(repository, never()).save(any(Reasignacion.class));
        // Verificamos que tampoco intente crear una nueva cita
        verify(citaRepository, never()).save(any(CitaMedica.class)); 
    }

    @Test
    void testProcesarCancelacion_ManejaExcepcionDelClienteFeign() {

        when(waitlistClient.obtenerSiguientePaciente("Dermatologia"))
                .thenThrow(new RuntimeException("Error de conexión"));

        cancelacionListener.procesarCancelacion(eventoDummy);

        verify(repository, never()).save(any(Reasignacion.class));
    }
}