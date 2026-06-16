package BinarySeint.AutoReasign_Service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "citas_medicas")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut_paciente")
    private String rutPaciente; 

    @Column(name = "especialidad_tipo")
    private String especialidadYTipo;

    private String medico;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora; 

    private String lugar;

    private String estado; 

    public CitaMedica() {
    }

    public CitaMedica(String rutPaciente, String especialidadYTipo, String medico, LocalDateTime fechaHora, String lugar, String estado) {
        this.rutPaciente = rutPaciente;
        this.especialidadYTipo = especialidadYTipo;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRutPaciente() {
        return rutPaciente;
    }

    public void setRutPaciente(String rutPaciente) {
        this.rutPaciente = rutPaciente;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
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