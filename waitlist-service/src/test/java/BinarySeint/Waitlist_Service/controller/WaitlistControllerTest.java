package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.EstadoPaciente;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import BinarySeint.Waitlist_Service.service.WaitlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaitlistController.class)
@AutoConfigureMockMvc(addFilters = false)
class WaitlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitlistService waitlistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WaitlistController waitlistController;

    @Test
    void testCrearRegistro_RetornaCreated() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("rutPaciente", "12345678-9");
        request.put("estado", "EN_ESPERA");
        request.put("prioridad", "Nivel 1");
        request.put("gesAuge", "true");

        RegistroPaciente dummy = new RegistroPaciente();
        dummy.setRutPaciente("12345678-9");

        when(waitlistService.guardarRegistroPaciente(anyString(), any(EstadoPaciente.class), anyString(), anyBoolean()))
                .thenReturn(dummy);

        mockMvc.perform(post("/api/espera/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rutPaciente").value("12345678-9"));
    }

    @Test
    void testObtenerEsperaPorPaciente_Existe_RetornaOk() throws Exception {
        RegistroPaciente dummy = new RegistroPaciente();
        when(waitlistService.obtenerRegistroPorRut("12345678-9")).thenReturn(dummy);

        mockMvc.perform(get("/api/espera/paciente/12345678-9"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerEsperaPorPaciente_NoExiste_RetornaNotFound() throws Exception {
        when(waitlistService.obtenerRegistroPorRut("12345678-9")).thenReturn(null);

        mockMvc.perform(get("/api/espera/paciente/12345678-9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testModificarEstado_RetornaOk() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("estado", "EN_ESPERA");
        request.put("prioridad", "Alta");
        
        RegistroPaciente dummy = new RegistroPaciente();
        // Usamos any() en lugar de anyString() para soportar de forma segura cualquier dato string o nulo
        when(waitlistService.modificarRegistroPorDoctor(anyString(), any(EstadoPaciente.class), any()))
                .thenReturn(dummy);

        mockMvc.perform(put("/api/espera/paciente/12345678-9/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testModificarEstado_EstadoInvalido_RetornaBadRequest() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("estado", "ESTADO_INVALIDO_XYZ");

        mockMvc.perform(put("/api/espera/paciente/12345678-9/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testModificarEstado_RuntimeException_RetornaNotFound() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("estado", "EN_ESPERA");
        request.put("prioridad", "Alta");

        // any() asegura que Mockito capture la llamada y lance la excepción correctamente
        when(waitlistService.modificarRegistroPorDoctor(anyString(), any(EstadoPaciente.class), any()))
                .thenThrow(new RuntimeException("Paciente no encontrado"));

        mockMvc.perform(put("/api/espera/paciente/12345678-9/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegistrarEnListaEspera_RetornaCreated() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("rutPaciente", "12345678-9");
        request.put("idEspecialidad", 1);
        request.put("tipoAtencion", "Urgencia");

        when(waitlistService.registrarEnListaEspera(anyString(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(new RegistroPaciente());

        mockMvc.perform(post("/api/espera/lista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testObtenerLista_RetornaOk() throws Exception {
        when(waitlistService.obtenerListaPorEspecialidad(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/espera/lista/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarDeLista_RetornaNoContent() throws Exception {
        when(waitlistService.eliminarRegistroPorRut("12345678-9")).thenReturn(true);

        mockMvc.perform(delete("/api/espera/lista/12345678-9"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarDeLista_Fallo_RetornaNotFound() throws Exception {
        when(waitlistService.eliminarRegistroPorRut("12345678-9")).thenReturn(false);

        mockMvc.perform(delete("/api/espera/lista/12345678-9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerSiguientePaciente_RetornaRut() throws Exception {
        when(waitlistService.obtenerYSacarSiguientePaciente("Urgencia")).thenReturn("12345678-9");

        mockMvc.perform(get("/api/espera/siguiente/Urgencia"))
                .andExpect(status().isOk())
                .andExpect(content().string("12345678-9"));
    }

    @Test
    void testFallbacks_RetornanServiceUnavailable() {
        ResponseEntity<RegistroPaciente> res1 = waitlistController.registrarListaFallback(new HashMap<>(), new RuntimeException("Error"));
        ResponseEntity<RegistroPaciente> res2 = waitlistController.obtenerEsperaPacienteFallback("123", new RuntimeException("Error"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, res1.getStatusCode());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, res2.getStatusCode());
    }
}