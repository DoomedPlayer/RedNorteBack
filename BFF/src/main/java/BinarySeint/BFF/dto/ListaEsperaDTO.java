package BinarySeint.BFF.dto;

public class ListaEsperaDTO {

    private String estado;         
    private String fechaRegistro;  
    private String prioridad;            

    public ListaEsperaDTO() {
    }

    public ListaEsperaDTO(String estado, String fechaRegistro, String prioridad) {
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.prioridad = prioridad;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

}