package BinarySeint.AutoReasign_Service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "cita_medica")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut_paciente")
    private String rutPaciente; 

    @Column(nullable = false)
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atencion", nullable = false)
    private TipoAtencion tipoAtencion;

    @Column(name = "medico")
    private String medico;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora; 

    @Column(name = "lugar")
    private String lugar;

    @Column(name = "estado")
    private String estado;

    public CitaMedica() {
    }

    public CitaMedica(String rutPaciente, String especialidad,TipoAtencion tipoAtencion, String medico, LocalDateTime fechaHora, String lugar, String estado) {
        this.rutPaciente = rutPaciente;
        this.especialidad = especialidad;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.tipoAtencion= tipoAtencion;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public TipoAtencion getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(TipoAtencion tipo) {
        this.tipoAtencion = tipo;
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