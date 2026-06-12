package BinarySeint.Waitlist_Service.dto;

public class ListaEsperaDTO {

    private String estado;         // Ej: "Pendiente de asignación médica"
    private String fechaRegistro;  // Ej: "14-04-2026"
    private String prioridad;      // Ej: "Media - Alta"
    private boolean gesAuge;       // true o false (para activar el banner verde de Cobertura Legal)

    // --- CONSTRUCTORES ---
    public ListaEsperaDTO() {
    }

    public ListaEsperaDTO(String estado, String fechaRegistro, String prioridad, boolean gesAuge) {
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.prioridad = prioridad;
        this.gesAuge = gesAuge;
    }

    // --- GETTERS Y SETTERS ---

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public boolean isGesAuge() {
        return gesAuge;
    }

    public void setGesAuge(boolean gesAuge) {
        this.gesAuge = gesAuge;
    }
}