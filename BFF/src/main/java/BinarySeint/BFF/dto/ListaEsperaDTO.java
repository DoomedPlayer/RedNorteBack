package BinarySeint.BFF.dto;

public class ListaEsperaDTO {

    private String estado;         
    private String fechaRegistro;  
    private String prioridad;      
    private boolean gesAuge;       

    public ListaEsperaDTO() {
    }

    public ListaEsperaDTO(String estado, String fechaRegistro, String prioridad, boolean gesAuge) {
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.prioridad = prioridad;
        this.gesAuge = gesAuge;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public boolean isGesAuge() { return gesAuge; }
    public void setGesAuge(boolean gesAuge) { this.gesAuge = gesAuge; }
}