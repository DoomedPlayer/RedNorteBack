package com.rednorte.portal;

// Estas dos líneas son vitales para que no te dé el error "cannot be resolved"
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class PatientPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientPortalApplication.class, args);
    }

}
