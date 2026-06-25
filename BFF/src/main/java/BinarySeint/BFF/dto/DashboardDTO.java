package BinarySeint.BFF.dto;

import java.util.List;

public class DashboardDTO {

    // 1. Bloque de Perfil del Paciente (Viene de Patient-Portal)
    private String nombreCompleto;
    private String rut;
    private String email;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaParentesco;
    private String contactoEmergenciaTelefono;

    // 2. Bloque de Prioridad Sanitaria / Lista de Espera (Viene de waitlist-service)
    private String estadoActual;
    private String fechaIngresoLista;
    private String prioridadAsignada;
    private boolean tieneCoberturaGesAuge;

    // 3. Listados (Próximas citas, Recetas y Exámenes)
    private List<CitaMedicaDTO> proximasCitas;
    private List<DocumentoDTO> recetasYExamenes;

    public DashboardDTO() {
    }

    public DashboardDTO(PacienteDTO paciente, ListaEsperaDTO listaEspera, 
                        List<CitaMedicaDTO> citas, List<DocumentoDTO> documentos) {
        
        if (paciente != null) {
            this.nombreCompleto = paciente.getNombreCompleto();
            this.rut = paciente.getRut();
            this.email = paciente.getCorreo();
            this.contactoEmergenciaNombre = paciente.getContactoEmergenciaNombre();
            this.contactoEmergenciaParentesco = paciente.getContactoEmergenciaParentesco();
            this.contactoEmergenciaTelefono = paciente.getContactoEmergenciaTelefono();
        }
        
        if (listaEspera != null) {
            this.estadoActual = listaEspera.getEstado();
            this.fechaIngresoLista = listaEspera.getFechaRegistro();
            this.prioridadAsignada = listaEspera.getPrioridad();
            this.tieneCoberturaGesAuge = listaEspera.isGesAuge();
        }

        this.proximasCitas = citas;
        this.recetasYExamenes = documentos;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactoEmergenciaNombre() {
        return contactoEmergenciaNombre;
    }

    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) {
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
    }

    public String getContactoEmergenciaParentesco() {
        return contactoEmergenciaParentesco;
    }

    public void setContactoEmergenciaParentesco(String contactoEmergenciaParentesco) {
        this.contactoEmergenciaParentesco = contactoEmergenciaParentesco;
    }

    public String getContactoEmergenciaTelefono() {
        return contactoEmergenciaTelefono;
    }

    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) {
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getFechaIngresoLista() {
        return fechaIngresoLista;
    }

    public void setFechaIngresoLista(String fechaIngresoLista) {
        this.fechaIngresoLista = fechaIngresoLista;
    }

    public String getPrioridadAsignada() {
        return prioridadAsignada;
    }

    public void setPrioridadAsignada(String prioridadAsignada) {
        this.prioridadAsignada = prioridadAsignada;
    }

    public boolean isTieneCoberturaGesAuge() {
        return tieneCoberturaGesAuge;
    }

    public void setTieneCoberturaGesAuge(boolean tieneCoberturaGesAuge) {
        this.tieneCoberturaGesAuge = tieneCoberturaGesAuge;
    }

    public List<CitaMedicaDTO> getProximasCitas() {
        return proximasCitas;
    }

    public void setProximasCitas(List<CitaMedicaDTO> proximasCitas) {
        this.proximasCitas = proximasCitas;
    }

    public List<DocumentoDTO> getRecetasYExamenes() {
        return recetasYExamenes;
    }

    public void setRecetasYExamenes(List<DocumentoDTO> recetasYExamenes) {
        this.recetasYExamenes = recetasYExamenes;
    }
}