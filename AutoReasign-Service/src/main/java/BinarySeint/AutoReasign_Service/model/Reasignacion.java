package BinarySeint.AutoReasign_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_reasignaciones")
public class Reasignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long idCitaOriginal;
    private String rutPacienteNuevo;
    private LocalDateTime fechaReasignacion;
    private String estado;

    public Reasignacion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCitaOriginal() {
        return idCitaOriginal;
    }

    public void setIdCitaOriginal(Long idCitaOriginal) {
        this.idCitaOriginal = idCitaOriginal;
    }

    public String getRutPacienteNuevo() {
        return rutPacienteNuevo;
    }

    public void setRutPacienteNuevo(String rutPacienteNuevo) {
        this.rutPacienteNuevo = rutPacienteNuevo;
    }

    public LocalDateTime getFechaReasignacion() {
        return fechaReasignacion;
    }

    public void setFechaReasignacion(LocalDateTime fechaReasignacion) {
        this.fechaReasignacion = fechaReasignacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}