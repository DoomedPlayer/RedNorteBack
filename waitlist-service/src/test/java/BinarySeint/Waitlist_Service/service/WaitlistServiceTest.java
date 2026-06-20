package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import BinarySeint.Waitlist_Service.repository.WaitlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitlistServiceTest {

    @Mock
    private WaitlistRepository repository;

    @InjectMocks
    private WaitlistService waitlistService;

    @Test
    void testRegistrarPaciente_GuardaEnRepositorio() {
        RegistroEspera dummyRegistro = new RegistroEspera();
        dummyRegistro.setRutPaciente("12345678-9");
        dummyRegistro.setIdEspecialidad(1);
        
        when(repository.save(any(RegistroEspera.class))).thenReturn(dummyRegistro);

        RegistroEspera resultado = waitlistService.registrarPaciente("12345678-9", 1, "Consulta", true);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRutPaciente());
        verify(repository, times(1)).save(any(RegistroEspera.class));
    }

    @Test
    void testObtenerListaPorEspecialidad_RetornaLista() {
        RegistroEspera dummy = new RegistroEspera();
        when(repository.findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(1, "EN_ESPERA"))
                .thenReturn(Collections.singletonList(dummy));

        List<RegistroEspera> lista = waitlistService.obtenerListaPorEspecialidad(1);

        assertFalse(lista.isEmpty());
        verify(repository, times(1)).findByIdEspecialidadAndEstadoOrderByNivelPrioridadAscFechaIngresoAsc(1, "EN_ESPERA");
    }
}