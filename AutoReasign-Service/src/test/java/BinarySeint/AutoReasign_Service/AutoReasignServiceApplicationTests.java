package BinarySeint.AutoReasign_Service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import BinarySeint.AutoReasign_Service.repository.CitaMedicaRepository;
import BinarySeint.AutoReasign_Service.repository.ReasignacionRepository;

@SpringBootTest
@Disabled("Esta prueba intenta levantar infraestructura real, se salta en CI/CD")
class AutoReasignServiceApplicationTests {

	@MockBean
    private CitaMedicaRepository citaRepository;

    @MockBean
    private ReasignacionRepository reasignacionRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        // Si llega hasta aquí, significa que la aplicación puede iniciar correctamente
    }

}
