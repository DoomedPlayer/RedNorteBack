package com.rednorte.portal.controllers;

import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortalController.class)
class PortalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteRepository pacienteRepository;

    @Test
    void testObtenerPacientePorRut_Existe_RetornaDTO() throws Exception {
        // Preparamos las entidades anidadas
        Persona persona = new Persona();
        persona.setPrimerNombre("Juan");
        persona.setApellidoPaterno("Perez");
        persona.setApellidoMaterno("Soto");
        persona.setEmail("juan@mail.com");

        Paciente paciente = new Paciente();
        paciente.setRutPaciente("12345678-9");
        paciente.setPersona(persona);

        when(pacienteRepository.findByRutPaciente("12345678-9")).thenReturn(Optional.of(paciente));

        mockMvc.perform(get("/api/v1/portal/pacientes/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez Soto"))
                .andExpect(jsonPath("$.correo").value("juan@mail.com"));
    }

    @Test
    void testObtenerPacientePorRut_NoExiste_Retorna404() throws Exception {
        when(pacienteRepository.findByRutPaciente("99999999-9")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/portal/pacientes/99999999-9"))
                .andExpect(status().isNotFound());
    }
}