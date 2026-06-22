package com.rednorte.portal.controllers.controller;

import com.rednorte.portal.controllers.MedicoController;
import com.rednorte.portal.entities.Medico;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.MedicoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
@AutoConfigureMockMvc(addFilters = false) // Evita bloqueos de Spring Security durante este test unitario
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoRepository medicoRepository;

    @Test
    void testObtenerMedicoPorRut_Existe_RetornaMedicoConDatosMapeados() throws Exception {
        // 1. Instanciamos las entidades anidadas de prueba (Persona y Medico)
        Persona personaDummy = new Persona();
        personaDummy.setPrimerNombre("Carlos");
        personaDummy.setApellidoPaterno("Andrade");

        Medico medicoDummy = new Medico();
        medicoDummy.setRutMedico("11223344-K");
        medicoDummy.setPersona(personaDummy);
        medicoDummy.setEspecialidad("Pediatría");
        medicoDummy.setIdEspecialidad(5);

        // 2. Simulamos el comportamiento del repositorio de médicos
        when(medicoRepository.findById("11223344-K")).thenReturn(Optional.of(medicoDummy));

        // 3. Ejecutamos la petición HTTP GET y evaluamos la estructura del JSON de respuesta
        mockMvc.perform(get("/api/portal/medicos/11223344-K")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11223344-K"))
                // Verificamos que concatene correctamente el prefijo "Dr. " con el nombre y apellido
                .andExpect(jsonPath("$.nombre").value("Dr. Carlos Andrade"))
                .andExpect(jsonPath("$.especialidad").value("Pediatría"))
                .andExpect(jsonPath("$.idEspecialidad").value(5));
    }

    @Test
    void testObtenerMedicoPorRut_NoExiste_Retorna404NotFound() throws Exception {
        // Simulamos que el médico buscado no se encuentra en el repositorio
        when(medicoRepository.findById("99999999-9")).thenReturn(Optional.empty());

        // Ejecutamos la petición esperando estrictamente un estado HTTP 404
        mockMvc.perform(get("/api/portal/medicos/99999999-9"))
                .andExpect(status().isNotFound());
    }
}