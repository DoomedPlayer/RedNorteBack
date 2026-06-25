package BinarySeint.AutoReasign_Service.service;

import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.model.TipoAtencion;
import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        citaDummy.setEspecialidad("MedicinaGeneral");
        citaDummy.setTipoAtencion(TipoAtencion.CIRUGIA);
        citaDummy.setFechaHora(LocalDateTime.now().plusDays(2));
        citaDummy.setEstado("ACTIVA");
        citaDummy.setLugar("Box 1");
        citaDummy.setMedico("Dr. House");
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
        
        CitaMedica resultado = citaMedicaService.crearCita(nuevaCita);

        assertEquals("ACTIVA", resultado.getEstado());
        assertNotNull(resultado.getId());
        verify(citaRepository, times(1)).save(any(CitaMedica.class));
    }

    @Test
    void testCrearCita_MantieneEstadoSiYaVieneAsignado() {
        CitaMedica citaConEstado = new CitaMedica();
        citaConEstado.setRutPaciente("98765432-1");
        citaConEstado.setEstado("PROGRAMADA");
        
        when(citaRepository.save(any(CitaMedica.class))).thenReturn(citaConEstado);
        
        CitaMedica resultado = citaMedicaService.crearCita(citaConEstado);

        assertEquals("PROGRAMADA", resultado.getEstado());
        verify(citaRepository, times(1)).save(any(CitaMedica.class));
    }

    @Test
    void testObtenerTodasLasCitas_RetornaLista() {
        when(citaRepository.findAll()).thenReturn(Arrays.asList(citaDummy));
        
        List<CitaMedica> resultado = citaMedicaService.obtenerTodasLasCitas();
        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerCitaPorId_RetornaCitaExitosa() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaDummy));
        
        CitaMedica resultado = citaMedicaService.obtenerCitaPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(citaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerCitaPorId_LanzaExcepcionSiNoExiste() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            citaMedicaService.obtenerCitaPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Cita no encontrada con el ID: 99"));
        verify(citaRepository, times(1)).findById(99L);
    }

    @Test
    void testObtenerCitasPorRutPaciente_RetornaLista() {
        when(citaRepository.findByRutPaciente("12345678-9")).thenReturn(Arrays.asList(citaDummy));
        
        List<CitaMedica> resultado = citaMedicaService.obtenerCitasPorRutPaciente("12345678-9");
        
        assertFalse(resultado.isEmpty());
        assertEquals("12345678-9", resultado.get(0).getRutPaciente());
        verify(citaRepository, times(1)).findByRutPaciente("12345678-9");
    }

    @Test
    void testObtenerCitasPorEspecialidad_RetornaLista() {
        when(citaRepository.findByEspecialidad("MedicinaGeneral")).thenReturn(Arrays.asList(citaDummy));
        
        List<CitaMedica> resultado = citaMedicaService.obtenerCitasPorEspecialidad("MedicinaGeneral");
        
        assertFalse(resultado.isEmpty());
        assertEquals("MedicinaGeneral", resultado.get(0).getEspecialidad());
        verify(citaRepository, times(1)).findByEspecialidad("MedicinaGeneral");
    }

    @Test
    void testActualizarCita_ModificaYRetornaCita() {
        CitaMedica datosActualizados = new CitaMedica();
        datosActualizados.setRutPaciente("99999999-9");
        datosActualizados.setEspecialidad("Cardiologia");
        datosActualizados.setEstado("REASIGNADA");

        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaDummy));
        when(citaRepository.save(any(CitaMedica.class))).thenReturn(citaDummy);
        
        CitaMedica resultado = citaMedicaService.actualizarCita(1L, datosActualizados);

        assertEquals("99999999-9", resultado.getRutPaciente());
        assertEquals("Cardiologia", resultado.getEspecialidad());
        assertEquals("REASIGNADA", resultado.getEstado());
        
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).save(citaDummy);
    }

    @Test
    void testEliminarCita_EjecutaDelete() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaDummy));
        doNothing().when(citaRepository).delete(citaDummy);
        
        citaMedicaService.eliminarCita(1L);
        
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).delete(citaDummy);
    }

    @Test
    void testCancelarCita_CambiaEstadoYEnviaEvento() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaDummy));
        when(citaRepository.save(any(CitaMedica.class))).thenReturn(citaDummy);

        String resultado = citaMedicaService.cancelarCita(1L);

        assertEquals("CANCELADA", citaDummy.getEstado());
        assertTrue(resultado.contains("ha sido CANCELADA"));

        verify(citaRepository, times(1)).save(citaDummy);
        verify(rabbitTemplate, times(1)).convertAndSend(
                anyString(), 
                anyString(), 
                any(EventoCancelacionDTO.class)
        );
    }
}