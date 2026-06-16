package com.rednorte.portal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @Column(name = "rut", length = 20)
    private String rut;

    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    // Mantienes tu ID original por si acaso
    @Column(name = "id_direccion")
    private Integer idDireccion;

    // NUEVOS CAMPOS ADAPTADOS A LA VISTA DEL PORTAL
    @Column(name = "direccion_texto", length = 255)
    private String direccionTexto;

    @Column(name = "contacto_emergencia_nombre", length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;
}