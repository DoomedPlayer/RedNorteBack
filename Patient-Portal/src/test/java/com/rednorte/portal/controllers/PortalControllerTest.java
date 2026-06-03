package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.repositories.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

// Importamos "get" y "post" para simular las peticiones
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortalController.class)
public class PortalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteRepository pacienteRepository;

    @Test
    public void testBuscarPorRutEncontrado() throws Exception {
        Paciente pacienteMock = Paciente.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Pedro")
                .apellidos("Pascal")
                .correo("pedro@rednorte.cl")
                .notificacionesActivas(true)
                .build();

        Mockito.when(pacienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(pacienteMock));

        mockMvc.perform(get("/api/v1/portal/pacientes/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombreCompleto").value("Pedro Pascal"))
                .andExpect(jsonPath("$.estadoListaEspera").value("Pendiente de asignación médica"));
    }

    @Test
    public void testBuscarPorRutNoEncontrado() throws Exception {
        Mockito.when(pacienteRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/portal/pacientes/99999999-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // 🌟 NUEVA PRUEBA: Registrar Paciente (POST)
    @Test
    public void testRegistrarPaciente() throws Exception {
        // 1. PREPARACIÓN: Simulamos cómo quedará el paciente después de guardarse (con un ID asignado)
        Paciente pacienteGuardado = Paciente.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Pedro")
                .apellidos("Pascal")
                .correo("pedro@rednorte.cl")
                .notificacionesActivas(true)
                .build();

        // Le decimos a Mockito: "Si alguien intenta guardar CUALQUIER paciente, devuelve el pacienteGuardado"
        Mockito.when(pacienteRepository.save(Mockito.any(Paciente.class))).thenReturn(pacienteGuardado);

        // Simulamos el JSON que enviaría el Swagger o React
        String pacienteJson = "{\n" +
                "  \"rut\": \"12345678-9\",\n" +
                "  \"nombre\": \"Pedro\",\n" +
                "  \"apellidos\": \"Pascal\",\n" +
                "  \"correo\": \"pedro@rednorte.cl\",\n" +
                "  \"notificacionesActivas\": true\n" +
                "}";

        // 2. ACCIÓN Y VERIFICACIÓN: Hacemos un POST
        mockMvc.perform(post("/api/v1/portal/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pacienteJson))
                .andExpect(status().isOk())
                // Verificamos que la respuesta incluya el ID generado (1)
                .andExpect(jsonPath("$.id").value(1));
    }
}