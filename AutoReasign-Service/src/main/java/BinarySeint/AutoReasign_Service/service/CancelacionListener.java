package BinarySeint.AutoReasign_Service.service ;

import BinarySeint.AutoReasign_Service.cliente.WaitlistClient;
import BinarySeint.AutoReasign_Service.dto.EventoCancelacionDTO;
import BinarySeint.AutoReasign_Service.model.Reasignacion;
import BinarySeint.AutoReasign_Service.repository.ReasignacionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CancelacionListener {

    @Autowired
    private ReasignacionRepository repository;

    @Autowired
    private WaitlistClient waitlistClient;

    @RabbitListener(queues = "citas.canceladas.queue")
    public void procesarCancelacion(EventoCancelacionDTO evento) {
        System.out.println("🚨 ATENCIÓN: Se ha cancelado la cita ID: " + evento.getIdCitaOriginal());
        try {
            // 1. Llamamos al microservicio de Lista de Espera por la red
            String rutReal = waitlistClient.obtenerSiguientePaciente(evento.getEspecialidad());
            
            if (rutReal != null && !rutReal.isEmpty()) {
                Reasignacion nuevaReasignacion = new Reasignacion();
                nuevaReasignacion.setIdCitaOriginal(evento.getIdCitaOriginal());
                nuevaReasignacion.setRutPacienteNuevo(rutReal);
                nuevaReasignacion.setFechaReasignacion(LocalDateTime.now());
                nuevaReasignacion.setEstado("COMPLETADA");

                repository.save(nuevaReasignacion);
                System.out.println("✅ Hora reasignada exitosamente al paciente: " + rutReal);
            } else {
                System.out.println("⚠️ No hay pacientes en espera para: " + evento.getEspecialidad());
            }

        } catch (Exception e) {
            System.err.println("❌ Error comunicándose con Waitlist-Service: " + e.getMessage());
        }
    }
}
