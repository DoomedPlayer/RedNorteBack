package BinarySeint.AutoReasign_Service.service;

import BinarySeint.AutoReasign_Service.config.RabbitMQConfig;
import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CitaMedicaService {

    @Autowired
    private CitaMedicaRepository citaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public CitaMedica crearCita(CitaMedica cita) {
        if (cita.getEstado() == null || cita.getEstado().isEmpty()) {
            cita.setEstado("ACTIVA");
        }
        return citaRepository.save(cita);
    }

    public List<CitaMedica> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    public CitaMedica obtenerCitaPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
    }
    
    public List<CitaMedica> obtenerCitasPorRutPaciente(String rutPaciente) {
        return citaRepository.findByRutPaciente(rutPaciente);
    }

    public List<CitaMedica> obtenerCitasPorEspecialidad(String especialidad) {
        return citaRepository.findByEspecialidad(especialidad);
    }

    @Transactional
    public CitaMedica actualizarCita(Long id, CitaMedica datosActualizados) {
        CitaMedica citaExistente = obtenerCitaPorId(id);
        
        citaExistente.setRutPaciente(datosActualizados.getRutPaciente());
        citaExistente.setEspecialidad(datosActualizados.getEspecialidad()); // <-- NUEVO
        citaExistente.setTipoAtencion(datosActualizados.getTipoAtencion());
        citaExistente.setMedico(datosActualizados.getMedico());
        citaExistente.setFechaHora(datosActualizados.getFechaHora());
        citaExistente.setLugar(datosActualizados.getLugar());
        citaExistente.setEstado(datosActualizados.getEstado());
        
        return citaRepository.save(citaExistente);
    }

    @Transactional
    public void eliminarCita(Long id) {
        CitaMedica cita = obtenerCitaPorId(id);
        citaRepository.delete(cita);
    }

    @Transactional
    public String cancelarCita(Long idCita) {
        CitaMedica cita = obtenerCitaPorId(idCita);
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);

        EventoCancelacionDTO evento = new EventoCancelacionDTO();
        evento.setIdCitaOriginal(cita.getId());
        evento.setEspecialidad(cita.getEspecialidad()); 
        evento.setRutPacienteCancelado(cita.getRutPaciente());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME, 
            RabbitMQConfig.ROUTING_KEY, 
            evento
        );
        
        return "Cita " + idCita + " del paciente " + cita.getRutPaciente() + " ha sido CANCELADA.";
    }
}