package com.rednorte.portal.Services;

import com.rednorte.portal.dtos.PacienteRequestDTO;
import com.rednorte.portal.dtos.PacienteResponseDTO;
import com.rednorte.portal.entities.Paciente;
import com.rednorte.portal.entities.Persona;
import com.rednorte.portal.repositories.PacienteRepository;
import com.rednorte.portal.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorPatientService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<PacienteResponseDTO> obtenerTodosLosPacientes() {
        return pacienteRepository.findAll().stream().map(paciente -> {
            // Manejo seguro para evitar NullPointerException si faltan datos
            Persona p = paciente.getPersona();
            String nombreCompleto = p != null ? p.getPrimerNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno() : "Paciente Sin Nombre";
            String rut = p != null ? p.getRut() : paciente.getRutPaciente();

            return PacienteResponseDTO.builder()
                    .rut(rut)
                    .nombre(nombreCompleto)
                    .edad(35) 
                    .prevision("Fonasa B") 
                    .estado("En Tratamiento") 
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public PacienteResponseDTO crearPaciente(PacienteRequestDTO request) {
        Persona persona = Persona.builder()
                .rut(request.getRut())
                .primerNombre(request.getPrimerNombre())
                .segundoNombre("")
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .email(request.getEmail())
                .build();
        personaRepository.save(persona);

        Paciente paciente = Paciente.builder()
                .rutPaciente(request.getRut())
                .persona(persona)
                .antecedentesMedicos(request.getAntecedentesMedicos())
                .build();
        pacienteRepository.save(paciente);

        return PacienteResponseDTO.builder()
                .rut(persona.getRut())
                .nombre(persona.getPrimerNombre() + " " + persona.getApellidoPaterno())
                .edad(30)
                .prevision("Isapre")
                .estado("Alta Médica")
                .build();
    }

    @Transactional
    public void eliminarPaciente(String rut) {
        // Manejo seguro de eliminación
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(rut);
        if (pacienteOpt.isPresent()) {
            pacienteRepository.delete(pacienteOpt.get());
        }
        
        Optional<Persona> personaOpt = personaRepository.findById(rut);
        if (personaOpt.isPresent()) {
            personaRepository.delete(personaOpt.get());
        }
    }
}