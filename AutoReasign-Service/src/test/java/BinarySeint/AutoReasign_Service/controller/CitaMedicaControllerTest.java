package BinarySeint.AutoReasign_Service.controller;

import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.model.TipoAtencion;
import BinarySeint.AutoReasign_Service.service.CitaMedicaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaMedicaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CitaMedicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaMedicaService citaMedicaService;

    @Autowired
    private ObjectMapper objectMapper;

    private CitaMedica citaDummy;

    @BeforeEach
    void setUp() {
        citaDummy = new CitaMedica();
        citaDummy.setId(1L);
        citaDummy.setRutPaciente("11223344-5");
        citaDummy.setEspecialidad("Cardiologia");
        citaDummy.setTipoAtencion(TipoAtencion.CONTROL);
        citaDummy.setEstado("ACTIVA");
    }

    @Test
    void testCrearCita_RetornaCreated() throws Exception {
        Mockito.when(citaMedicaService.crearCita(any(CitaMedica.class))).thenReturn(citaDummy);

        mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(citaDummy))) 
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }

    @Test
    void testObtenerPorId_RetornaCita() throws Exception {
        Mockito.when(citaMedicaService.obtenerCitaPorId(1L)).thenReturn(citaDummy);

        mockMvc.perform(get("/api/citas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rutPaciente").value("11223344-5"));
    }

    @Test
    void testCancelarCita_RetornaOk() throws Exception {
        String mensajeEsperado = "Cita 1 del paciente 11223344-5 ha sido CANCELADA.";
        Mockito.when(citaMedicaService.cancelarCita(1L)).thenReturn(mensajeEsperado);

        mockMvc.perform(post("/api/citas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(content().string(mensajeEsperado));
    }
}