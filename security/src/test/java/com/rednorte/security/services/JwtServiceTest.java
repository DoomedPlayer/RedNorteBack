package com.rednorte.security.services;

import com.rednorte.security.entity.Rol;
import com.rednorte.security.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private Usuario usuarioDummy;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "T1VaRzZ4bU03T1B5bkhsWEtKVXo5Z01DckJWaTJGQ3E4RjNEOWEyYjVjN2UxZjRk");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        usuarioDummy = new Usuario();
        usuarioDummy.setRutPersona("12345678-9");
        usuarioDummy.setRol(Rol.PACIENTE);
    }

    @Test
    void testGenerateToken_CreaTokenValido() {
        String token = jwtService.generateToken(usuarioDummy);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }
}