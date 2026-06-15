package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortalController.class)
public class PortalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteRepository pacienteRepository;

    @Test
    public void obtenerPacientePorRut_Exito() throws Exception {
        // 1. Armamos la Persona (Nuevo modelo 3NF)
        Persona persona = Persona.builder()
                .rut("12345678-9")
                .primerNombre("Pedro")
                .apellidoPaterno("Pascal")
                .apellidoMaterno("Balmaceda")
                .email("pedro.pascal@rednorte.cl")
                .build();

        // 2. Armamos el Paciente asociado a esa Persona
        Paciente paciente = Paciente.builder()
                .rutPaciente("12345678-9")
                .persona(persona)
                .build();

        // 3. Simulamos el comportamiento usando el nuevo método "findByRutPaciente"
        Mockito.when(pacienteRepository.findByRutPaciente("12345678-9")).thenReturn(Optional.of(paciente));

        // 4. Ejecutamos la petición y verificamos que traiga el nombre completo
        mockMvc.perform(get("/api/v1/portal/pacientes/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Pedro Pascal Balmaceda"))
                .andExpect(jsonPath("$.correo").value("pedro.pascal@rednorte.cl"));
    }

    @Test
    public void obtenerPacientePorRut_NoEncontrado() throws Exception {
        // Simulamos que el RUT no existe
        Mockito.when(pacienteRepository.findByRutPaciente("00000000-0")).thenReturn(Optional.empty());

        // Verificamos que devuelva el código HTTP 404 (Not Found)
        mockMvc.perform(get("/api/v1/portal/pacientes/00000000-0"))
                .andExpect(status().isNotFound());
    }
}