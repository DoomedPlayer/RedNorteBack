package BinarySeint.AutoReasign_Service.dto;

public class EventoCancelacionDTO {
    
    private Long idCitaOriginal;
    private String especialidad;
    private String rutPacienteCancelado;

    public EventoCancelacionDTO() {}

    public Long getIdCitaOriginal() {
        return idCitaOriginal;
    }

    public void setIdCitaOriginal(Long idCitaOriginal) {
        this.idCitaOriginal = idCitaOriginal;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getRutPacienteCancelado() {
        return rutPacienteCancelado;
    }

    public void setRutPacienteCancelado(String rutPacienteCancelado) {
        this.rutPacienteCancelado = rutPacienteCancelado;
    }
}
