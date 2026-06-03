package com.rednorte.portal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Magia de Lombok: crea Getters y Setters automáticamente
@Builder // Patrón de diseño para crear objetos fácilmente (útil para las pruebas de Ev3)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Le dice a Spring Boot que esto será una tabla en MySQL
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut", unique = true, nullable = false, length = 12)
    private String rut;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 50)
    private String apellidos;

    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    // Dato extra útil para el portal: saber si el paciente tiene notificaciones pendientes
    @Column(name = "notificaciones_activas")
    private Boolean notificacionesActivas = false;
}