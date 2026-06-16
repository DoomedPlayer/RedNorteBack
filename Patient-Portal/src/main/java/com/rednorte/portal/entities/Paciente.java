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
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(name = "rut_paciente", length = 20)
    private String rutPaciente;

    @Column(name = "antecedentes_medicos", columnDefinition = "TEXT")
    private String antecedentesMedicos;

    /*
     * RELACIÓN 1 A 1 CON PERSONA
     * @MapsId hace que la llave primaria de Paciente sea automáticamente 
     * la llave foránea compartida con la tabla Persona (mismo RUT).
     */
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "rut_paciente")
    private Persona persona;
}