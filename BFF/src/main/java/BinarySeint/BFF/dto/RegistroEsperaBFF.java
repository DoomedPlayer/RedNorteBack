package BinarySeint.BFF.dto;

import java.time.LocalDate;

public class RegistroEsperaBFF {
    private String estado;
    private LocalDate fechaIngreso;
    private Integer nivelPrioridad;
    private boolean gesAuge;

    // Getters
    public String getEstado() { return estado; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public Integer getNivelPrioridad() { return nivelPrioridad; }
    public boolean isGesAuge() { return gesAuge; }

    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public void setNivelPrioridad(Integer nivelPrioridad) { this.nivelPrioridad = nivelPrioridad; }
    public void setGesAuge(boolean gesAuge) { this.gesAuge = gesAuge; }
}
