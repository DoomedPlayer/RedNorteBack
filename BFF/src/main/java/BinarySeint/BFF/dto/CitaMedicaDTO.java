package BinarySeint.BFF.dto;

public class CitaMedicaDTO {

    private Long id;
    private String especialidad; 
    private String tipoAtencion;
    private String medico;            
    private String fechaHora;         
    private String lugar;
    private String estado;        

    public CitaMedicaDTO() {
    }

    public CitaMedicaDTO(String especialidad, String tipoAtencion, String medico, String fechaHora, String lugar,String estado) {
        this.especialidad = especialidad;
        this.tipoAtencion = tipoAtencion;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.estado = estado;
    }


    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(String tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }

    public String getMedico() {
        return medico;
    }
    public Long getId() { return id; }

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

     public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}