package BinarySeint.Waitlist_Service.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lista_espera")
public class ListaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Long idLista;

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

    @Column(name = "ges_auge", nullable = false)
    private boolean gesAuge;
    
    public ListaEspera() {}

    public Long getIdLista() { return idLista; }
    public void setIdLista(Long idLista) { this.idLista = idLista; }

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

    public boolean isGesAuge() { return gesAuge; }
    public void setGesAuge(boolean gesAuge) { this.gesAuge = gesAuge; }
}