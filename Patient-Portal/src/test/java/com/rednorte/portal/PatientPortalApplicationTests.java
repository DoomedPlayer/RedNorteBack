package com.rednorte.portal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Esta prueba intenta levantar infraestructura real, se salta en CI/CD")
public class PatientPortalApplicationTests {

    @Test
	void contextLoads() {
	}
}
