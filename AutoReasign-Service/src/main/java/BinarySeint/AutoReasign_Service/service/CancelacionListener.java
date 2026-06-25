package BinarySeint.AutoReasign_Service.service ;

import BinarySeint.AutoReasign_Service.cliente.WaitlistClient;
import BinarySeint.AutoReasign_Service.config.RabbitMQConfig;
import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.CitaMedica;
import BinarySeint.AutoReasign_Service.model.Reasignacion;
import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import BinarySeint.AutoReasign_Service.repository.ReasignacionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CancelacionListener {

    @Autowired
    private ReasignacionRepository reasignacionRepo;

    @Autowired
    private CitaMedicaRepository citaRepository;

    @Autowired
    private WaitlistClient waitlistClient;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) 
    public void procesarCancelacion(EventoCancelacionDTO evento) {
        System.out.println("🚨 ATENCIÓN: Procesando cancelación de cita ID: " + evento.getIdCitaOriginal());
        try {
            String rutReal = waitlistClient.obtenerSiguientePaciente(evento.getEspecialidad());
            
            if (rutReal != null && !rutReal.isEmpty()) {
                CitaMedica citaAntigua = citaRepository.findById(evento.getIdCitaOriginal()).orElse(null);
                
                if (citaAntigua != null) {
                    CitaMedica nuevaCita = new CitaMedica();
                    nuevaCita.setRutPaciente(rutReal);
                    nuevaCita.setEspecialidad(citaAntigua.getEspecialidad());
                    nuevaCita.setTipoAtencion(citaAntigua.getTipoAtencion());
                    nuevaCita.setMedico(citaAntigua.getMedico());
                    nuevaCita.setFechaHora(citaAntigua.getFechaHora());
                    nuevaCita.setLugar(citaAntigua.getLugar());
                    nuevaCita.setEstado("PROGRAMADA"); 
                    citaRepository.save(nuevaCita);

                    Reasignacion nuevaReasignacion = new Reasignacion();
                    nuevaReasignacion.setIdCitaOriginal(evento.getIdCitaOriginal());
                    nuevaReasignacion.setRutPacienteNuevo(rutReal);
                    nuevaReasignacion.setFechaReasignacion(LocalDateTime.now());
                    nuevaReasignacion.setEstado("COMPLETADA");
                    reasignacionRepo.save(nuevaReasignacion);

                    System.out.println("✅ Hora reasignada exitosamente. Nueva cita creada para: " + rutReal);
                }
            } else {
                System.out.println("⚠️ No hay pacientes en espera para: " + evento.getEspecialidad());
            }

        } catch (Exception e) {
            System.err.println("❌ Error en reasignación: " + e.getMessage());
        }
    }
}