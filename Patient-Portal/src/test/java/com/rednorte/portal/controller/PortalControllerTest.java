package com.rednorte.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.portal.controllers.PortalController;
import com.rednorte.portal.entities.Documento;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.DocumentoRepository;
import com.rednorte.portal.repositories.PacienteRepository;
import com.rednorte.portal.repositories.PersonaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortalController.class)
@AutoConfigureMockMvc(addFilters = false)
class PortalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteRepository pacienteRepository;

    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private DocumentoRepository documentoRepository; 

    @Autowired
    private ObjectMapper objectMapper;

    private Paciente pacienteDummy;
    private Persona personaDummy;

    @BeforeEach
    void setUp() {
        personaDummy = new Persona();
        personaDummy.setPrimerNombre("Juan");
        personaDummy.setApellidoPaterno("Perez");
        personaDummy.setApellidoMaterno("Soto");
        personaDummy.setEmail("juan@mail.com");

        pacienteDummy = new Paciente();
        pacienteDummy.setRutPaciente("12345678-9");
        pacienteDummy.setPersona(personaDummy);
        pacienteDummy.setContactoEmergenciaNombre("Maria Perez");
        pacienteDummy.setContactoEmergenciaParentesco("Hermana");
        pacienteDummy.setContactoEmergenciaTelefono("+56911223344");
    }

    @Test
    void testObtenerPacientePorRut_Existe_RetornaDTO() throws Exception {
        when(pacienteRepository.findByRutPaciente("12345678-9")).thenReturn(Optional.of(pacienteDummy));

        mockMvc.perform(get("/api/portal/pacientes/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez Soto"))
                .andExpect(jsonPath("$.correo").value("juan@mail.com"))
                .andExpect(jsonPath("$.contactoEmergenciaNombre").value("Maria Perez"));
    }

    @Test
    void testObtenerPacientePorRut_NoExiste_Retorna404() throws Exception {
        when(pacienteRepository.findByRutPaciente("99999999-9")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/portal/pacientes/99999999-9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerDocumentosPaciente_RetornaListaDeDTOs() throws Exception {
        Documento docDummy = new Documento();
        docDummy.setRutPaciente("12345678-9");
        docDummy.setNombreDocumento("Receta Médica");
        docDummy.setEmisorYFecha("Dr. House - 2026-06-25");
        docDummy.setUrlDescarga("http://aws.s3/receta.pdf");

        when(documentoRepository.findByRutPaciente("12345678-9")).thenReturn(Arrays.asList(docDummy));

        mockMvc.perform(get("/api/portal/pacientes/12345678-9/documentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombreDocumento").value("Receta Médica"))
                .andExpect(jsonPath("$[0].urlDescarga").value("http://aws.s3/receta.pdf"));
    }

    @Test
    void testCrearPerfilPacienteDesdeAuth_GuardaEntidadesYRetornaOk() throws Exception {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("rut", "98765432-1");
        requestData.put("nombre", "Pedro");
        requestData.put("apellidoPaterno", "Pascal");
        requestData.put("correo", "pedro@mail.com");

        when(personaRepository.save(any(Persona.class))).thenReturn(new Persona());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(new Paciente());

        mockMvc.perform(post("/api/portal/pacientes/registro-perfil")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk());

        verify(personaRepository, times(1)).save(any(Persona.class));
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void testObtenerTodosLosPacientes_RetornaListaConNombresConcatenados() throws Exception {
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(pacienteDummy));

        mockMvc.perform(get("/api/portal/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Juan Perez"));
    }
}