package BinarySeint.BFF.dto;

import java.time.LocalDate;

public class RegistroPacienteBFF {
    private String rutPaciente;
    private String estado;
    private LocalDate fechaRegistro;
    private String prioridad;
    private boolean gesAuge;

    // Getters
    public String getRutPaciente() { return rutPaciente; }

    public String getEstado() { return estado; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public boolean isGesAuge() { return gesAuge; }

    // Setters
    public void setRutPaciente(String rutPaciente) { this.rutPaciente = rutPaciente; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setGesAuge(boolean gesAuge) { this.gesAuge = gesAuge; }
}
