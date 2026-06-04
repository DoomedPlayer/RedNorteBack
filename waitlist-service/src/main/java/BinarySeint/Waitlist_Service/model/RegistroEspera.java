package BinarySeint.Waitlist_Service.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registro_espera")
public class RegistroEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long idRegistro;

    @Column(name = "rut_paciente", nullable = false, length = 12)
    private String rutPaciente;

    @Column(name = "id_especialidad", nullable = false)
    private Integer idEspecialidad; 

    @Column(name = "tipo_atencion", nullable = false)
    private String tipoAtencion;

    @Column(name = "nivel_prioridad", nullable = false)
    private Integer nivelPrioridad;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso; 
    
    @Column(nullable = false)
    private String estado;

    public RegistroEspera() {}

    // --- Getters y Setters ---

    public Long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Long idRegistro) { this.idRegistro = idRegistro; }

    public String getRutPaciente() { return rutPaciente; }
    public void setRutPaciente(String rutPaciente) { this.rutPaciente = rutPaciente; }

    public Integer getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Integer idEspecialidad) { this.idEspecialidad = idEspecialidad; }

    public String getTipoAtencion() { return tipoAtencion; }
    public void setTipoAtencion(String tipoAtencion) { this.tipoAtencion = tipoAtencion; }

    public Integer getNivelPrioridad() { return nivelPrioridad; }
    public void setNivelPrioridad(Integer nivelPrioridad) { this.nivelPrioridad = nivelPrioridad; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}