package com.rednorte.portal.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @Column(name = "rut_medico", length = 20)
    private String rutMedico;

    @Column(nullable = false)
    private String especialidad;

    @Column(name = "id_especialidad", nullable = false)
    private Integer idEspecialidad;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rut_medico")
    private Persona persona;

    public Medico() {}

    public Medico(String rutMedico,String especialidad,Integer idEspecialidad, Persona persona){
        this.rutMedico = rutMedico;
        this.especialidad = especialidad;
        this.idEspecialidad = idEspecialidad;
        this.persona = persona;
    }
    // --- GETTERS Y SETTERS ---
    public String getRutMedico() { return rutMedico; }
    public void setRutMedico(String rutMedico) { this.rutMedico = rutMedico; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Integer getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Integer idEspecialidad) { this.idEspecialidad = idEspecialidad; }
}