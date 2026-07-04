package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.factory.WaitlistFactoryProvider;
import BinarySeint.Waitlist_Service.model.EstadoPaciente;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import BinarySeint.Waitlist_Service.repository.ListaEsperaRepository;
import BinarySeint.Waitlist_Service.repository.RegistroPacienteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitlistServiceTest {

    @Mock private ListaEsperaRepository listaEsperaRepo;
    @Mock private RegistroPacienteRepository registroPacienteRepo;
    @Mock private WaitlistFactoryProvider factoryProvider;
    @Mock private WaitlistFactoryMethod mockFactory;

    @InjectMocks
    private WaitlistService waitlistService;

    @Test
    void testGuardarRegistroPaciente_GuardaCorrectamente() {
        RegistroPaciente paciente = new RegistroPaciente();
        when(registroPacienteRepo.findById(anyString())).thenReturn(Optional.of(paciente));
        when(registroPacienteRepo.save(any(RegistroPaciente.class))).thenReturn(paciente);

        RegistroPaciente resultado = waitlistService.guardarRegistroPaciente("123", EstadoPaciente.EN_ESPERA, "Nivel 1");

        assertNotNull(resultado);
        verify(registroPacienteRepo).save(any(RegistroPaciente.class));
    }

    @Test
    void testObtenerRegistroPorRut_Existe_RetornaPaciente() {
        RegistroPaciente paciente = new RegistroPaciente();
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.of(paciente));

        RegistroPaciente resultado = waitlistService.obtenerRegistroPorRut("123");

        assertNotNull(resultado);
    }

    @Test
    void testObtenerRegistroPorRut_NoExiste_RetornaNull() {
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.empty());

        RegistroPaciente resultado = waitlistService.obtenerRegistroPorRut("123");

        assertNull(resultado);
    }

    @Test
    void testModificarRegistroPorDoctor_Exito() {
        RegistroPaciente paciente = new RegistroPaciente();
        paciente.setRutPaciente("123");
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.of(paciente));
        when(registroPacienteRepo.save(any(RegistroPaciente.class))).thenReturn(paciente);

        RegistroPaciente resultado = waitlistService.modificarRegistroPorDoctor("123", EstadoPaciente.EN_ESPERA, "Nueva Prioridad");

        assertNotNull(resultado);
        assertEquals("Nueva Prioridad", resultado.getPrioridad());
    }

    @Test
    void testModificarRegistroPorDoctor_NoEncontrado_LanzaExcepcion() {
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            waitlistService.modificarRegistroPorDoctor("123", EstadoPaciente.EN_ESPERA, "Prioridad");
        });
    }

    @Test
    void testRegistrarEnListaEspera_CreaRegistro() {
        when(factoryProvider.obtenerFabrica(anyString())).thenReturn(mockFactory);

        ListaEspera ticketDummy = new ListaEspera();
        ticketDummy.setNivelPrioridad(1); 
        
        when(mockFactory.crearRegistro(anyString(), anyInt())).thenReturn(ticketDummy);
        when(registroPacienteRepo.findById(anyString())).thenReturn(Optional.of(new RegistroPaciente()));
        when(registroPacienteRepo.save(any(RegistroPaciente.class))).thenReturn(new RegistroPaciente());

        waitlistService.registrarEnListaEspera("123", 1, "Cirugía");

        verify(listaEsperaRepo).save(any(ListaEspera.class));
    }

    @Test
    void testObtenerListaPorEspecialidad() {
        List<ListaEspera> listaDummy = Collections.singletonList(new ListaEspera());
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(1)).thenReturn(listaDummy);

        List<ListaEspera> resultado = waitlistService.obtenerListaPorEspecialidad(1);

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testEliminarRegistroPorRut_Exito() {
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.of(new RegistroPaciente()));
        
        boolean resultado = waitlistService.eliminarRegistroPorRut("123");

        assertTrue(resultado);
        verify(listaEsperaRepo).deleteByRutPaciente("123");
    }

    @Test
    void testEliminarRegistroPorRut_ManejaError() {
        doThrow(new RuntimeException("Error simulado")).when(listaEsperaRepo).deleteByRutPaciente(anyString());

        boolean resultado = waitlistService.eliminarRegistroPorRut("123");

        assertFalse(resultado);
    }

    @Test
    void testObtenerYSacarSiguientePaciente_Urgencia_RetornaRut() {
        ListaEspera paciente = new ListaEspera();
        paciente.setRutPaciente("123");
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(1))
                .thenReturn(Collections.singletonList(paciente));
        when(registroPacienteRepo.findById("123")).thenReturn(Optional.of(new RegistroPaciente()));

        String rut = waitlistService.obtenerYSacarSiguientePaciente("Urgencia");

        assertEquals("123", rut);
    }

    @Test
    void testObtenerYSacarSiguientePaciente_Cirugia() {
        ListaEspera paciente = new ListaEspera();
        paciente.setRutPaciente("456");
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(2))
                .thenReturn(Collections.singletonList(paciente));
        when(registroPacienteRepo.findById("456")).thenReturn(Optional.of(new RegistroPaciente()));

        String rut = waitlistService.obtenerYSacarSiguientePaciente("Cirugía");

        assertEquals("456", rut);
    }

    @Test
    void testObtenerYSacarSiguientePaciente_Procedimiento() {
        ListaEspera paciente = new ListaEspera();
        paciente.setRutPaciente("789");
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(3))
                .thenReturn(Collections.singletonList(paciente));
        when(registroPacienteRepo.findById("789")).thenReturn(Optional.of(new RegistroPaciente()));

        String rut = waitlistService.obtenerYSacarSiguientePaciente("Procedimiento");

        assertEquals("789", rut);
    }

    @Test
    void testObtenerYSacarSiguientePaciente_ListaVacia_RetornaNull() {
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(5))
                .thenReturn(Collections.emptyList());

        String rut = waitlistService.obtenerYSacarSiguientePaciente("General");

        assertNull(rut);
    }
}