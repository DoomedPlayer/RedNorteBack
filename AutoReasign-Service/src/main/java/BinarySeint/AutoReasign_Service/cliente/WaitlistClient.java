package BinarySeint.AutoReasign_Service.cliente;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "waitlist-service", url = "http://waitlist-service:8081")
public interface WaitlistClient {

    @GetMapping("/api/espera/siguiente/{especialidad}")
    String obtenerSiguientePaciente(@PathVariable("especialidad") String especialidad);
}