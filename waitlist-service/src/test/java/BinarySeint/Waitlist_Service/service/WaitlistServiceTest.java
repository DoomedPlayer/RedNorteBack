package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.factory.WaitlistFactoryProvider;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitlistServiceTest {

    // Ahora inyectamos los repositorios y la fábrica correctos
    @Mock
    private ListaEsperaRepository listaEsperaRepo;

    @Mock
    private RegistroPacienteRepository registroPacienteRepo;

    @Mock
    private WaitlistFactoryProvider factoryProvider;

    @Mock
    private WaitlistFactoryMethod mockFactory;

    @InjectMocks
    private WaitlistService waitlistService;

    @Test
    void testRegistrarEnListaEspera_GuardaEnAmbosRepositorios() {
        // 1. Configuramos el mock de la fábrica
        when(factoryProvider.obtenerFabrica(anyString())).thenReturn(mockFactory);
        
        ListaEspera mockTicket = new ListaEspera();
        mockTicket.setNivelPrioridad(2);
        when(mockFactory.crearRegistro(anyString(), anyInt(), anyBoolean())).thenReturn(mockTicket);

        // 2. Configuramos el mock del perfil del paciente
        RegistroPaciente dummyPaciente = new RegistroPaciente();
        dummyPaciente.setRutPaciente("12345678-9");
        when(registroPacienteRepo.findById(anyString())).thenReturn(Optional.empty());
        when(registroPacienteRepo.save(any(RegistroPaciente.class))).thenReturn(dummyPaciente);

        // 3. Ejecutamos el servicio
        RegistroPaciente resultado = waitlistService.registrarEnListaEspera("12345678-9", 1, "Consulta", true);

        // 4. Verificamos que se haya ejecutado todo correctamente
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRutPaciente());
        
        // Verificamos que se guardó el ticket y se guardó el perfil del paciente
        verify(listaEsperaRepo, times(1)).save(any(ListaEspera.class));
        verify(registroPacienteRepo, times(1)).save(any(RegistroPaciente.class));
    }

    @Test
    void testObtenerListaPorEspecialidad_RetornaLista() {
        ListaEspera dummy = new ListaEspera();
        // Usamos el nombre del método correcto que no filtra por estado (porque ahora es exclusivo de la lista)
        when(listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(1))
                .thenReturn(Collections.singletonList(dummy));

        List<ListaEspera> lista = waitlistService.obtenerListaPorEspecialidad(1);

        assertFalse(lista.isEmpty());
        verify(listaEsperaRepo, times(1)).findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(1);
    }
}