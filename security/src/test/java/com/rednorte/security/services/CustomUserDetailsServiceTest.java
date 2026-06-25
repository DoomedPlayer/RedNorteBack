package com.rednorte.security.services;

import com.rednorte.security.entity.Rol;
import com.rednorte.security.entity.Usuario;
import com.rednorte.security.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private Usuario usuarioDummy;

    @BeforeEach
    void setUp() {
        usuarioDummy = new Usuario();
        usuarioDummy.setRutPersona("12345678-9");
        usuarioDummy.setPassword("hashed");
        usuarioDummy.setRol(Rol.PACIENTE);
    }

    @Test
    void testLoadUserByUsername_UsuarioExiste_RetornaUserDetails() {
        when(usuarioRepository.findByRutPersona("12345678-9")).thenReturn(Optional.of(usuarioDummy));

        UserDetails result = userDetailsService.loadUserByUsername("12345678-9");

        assertNotNull(result);
        // En tu implementación, getRutPersona() actúa como el username
        assertEquals("12345678-9", result.getUsername());
        verify(usuarioRepository, times(1)).findByRutPersona("12345678-9");
    }

    @Test
    void testLoadUserByUsername_UsuarioNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findByRutPersona("99999999-9")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("99999999-9");
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado con RUT: 99999999-9"));
        verify(usuarioRepository, times(1)).findByRutPersona("99999999-9");
    }
}