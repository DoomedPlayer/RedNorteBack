package BinarySeint.AutoReasign_Service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reasignacion")
public class ReasignacionController {

    @PostMapping("/ejecutar")
    public ResponseEntity<String> ejecutarReasignacionManual() {
        
        // Aquí puedes inyectar un servicio que busque citas caídas que no se procesaron por RabbitMQ
        // Por ahora devolveremos una respuesta exitosa básica.
        
        return ResponseEntity.ok("Proceso de reasignación manual iniciado correctamente en RedNorte.");
    }
}