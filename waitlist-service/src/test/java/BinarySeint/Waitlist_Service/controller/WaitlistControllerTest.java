package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import BinarySeint.Waitlist_Service.service.WaitlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

   @Test
    void testRegistrarEnLista_RetornaCreated() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("rutPaciente", "11223344-5");
        request.put("idEspecialidad", 1);
        request.put("tipoAtencion", "Control");
        request.put("gesAuge", false);

        // Ahora el controlador devuelve un RegistroPaciente
        RegistroPaciente dummy = new RegistroPaciente();
        dummy.setRutPaciente("11223344-5");

        // Llamamos al nuevo método del servicio
        when(waitlistService.registrarEnListaEspera(anyString(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(dummy);

        // El endpoint ahora es /api/espera/lista
        mockMvc.perform(post("/api/espera/lista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rutPaciente").value("11223344-5"));
    }

    @Test
    void testObtenerLista_RetornaOk() throws Exception {
        when(waitlistService.obtenerListaPorEspecialidad(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/espera/lista/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}