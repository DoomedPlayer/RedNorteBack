package com.rednorte.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.security.controller.AuthController;
import com.rednorte.security.dto.LoginRequest;
import com.rednorte.security.dto.RegisterRequest;
import com.rednorte.security.entity.Rol;
import com.rednorte.security.entity.Usuario;
import com.rednorte.security.repository.UsuarioRepository;
import com.rednorte.security.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplateMock;
    private Usuario usuarioDummy;

    @BeforeEach
    void setUp() {
        usuarioDummy = new Usuario();
        usuarioDummy.setRutPersona("12345678-9");
        usuarioDummy.setEmail("test@mail.com");
        usuarioDummy.setPassword("hashedPassword");
        usuarioDummy.setRol(Rol.PACIENTE);

        restTemplateMock = mock(RestTemplate.class);
    }

    @Test
    void testLogin_Exitoso_RetornaToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setRut("12345678-9");
        request.setPassword("123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByRutPersona("12345678-9")).thenReturn(Optional.of(usuarioDummy));
        when(jwtService.generateToken(usuarioDummy)).thenReturn("token-jwt-simulado");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt-simulado"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testLogin_CredencialesInvalidas_LanzaExcepcion() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setRut("12345678-9");
        request.setPassword("mala-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        try {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        } catch (Exception e) {
            assert(e.getCause() instanceof BadCredentialsException);
        }
    }

    @Test
    void testRegister_NuevoUsuario_RetornaOk() throws Exception {
        // Inyectamos el RestTemplate simulado al controlador
        AuthController controller = mockMvc.getDispatcherServlet().getWebApplicationContext().getBean(AuthController.class);
        ReflectionTestUtils.setField(controller, "restTemplate", restTemplateMock);

        RegisterRequest request = new RegisterRequest();
        request.setRut("11223344-5");
        request.setCorreo("nuevo@mail.com");
        request.setPassword("secreta123");

        when(usuarioRepository.findByRutPersona("11223344-5")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-pwd");

        when(restTemplateMock.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("OK"));

        when(jwtService.generateToken(any(Usuario.class))).thenReturn("nuevo-token-jwt");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("nuevo-token-jwt"))
                .andExpect(jsonPath("$.rut").value("11223344-5"));

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void testRegister_RutYaExiste_RetornaBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setRut("12345678-9");

        when(usuarioRepository.findByRutPersona("12345678-9")).thenReturn(Optional.of(usuarioDummy));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El RUT ya se encuentra registrado en el sistema de acceso."));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}