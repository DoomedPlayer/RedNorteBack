package com.rednorte.portal.dtos;

public class CitaMedicaDTO {

    private String especialidadYTipo; 
    private String medico;            
    private String fechaHora;         
    private String lugar;             

    public CitaMedicaDTO() {
    }

    public CitaMedicaDTO(String especialidadYTipo, String medico, String fechaHora, String lugar) {
        this.especialidadYTipo = especialidadYTipo;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
    }


    public String getEspecialidadYTipo() {
        return especialidadYTipo;
    }

    public void setEspecialidadYTipo(String especialidadYTipo) {
        this.especialidadYTipo = especialidadYTipo;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}