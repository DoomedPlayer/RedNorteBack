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

    @Column(name = "contacto_emergencia_nombre", length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_parentesco", length = 50)
    private String contactoEmergenciaParentesco;

    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "prevision", length = 20)
    private TipoPrevision prevision;

    @Column(name = "es_ges", columnDefinition = "boolean default false")
    private Boolean esGes;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rut_paciente")
    private Persona persona;
}