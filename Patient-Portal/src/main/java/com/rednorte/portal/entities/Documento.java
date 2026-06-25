package com.rednorte.portal.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamos el documento directamente con el RUT del paciente
    @Column(name = "rut_paciente", nullable = false)
    private String rutPaciente;

    @Column(name = "nombre_documento")
    private String nombreDocumento;

    @Column(name = "emisor_y_fecha")
    private String emisorYFecha;

    @Column(name = "url_descarga")
    private String urlDescarga;

    public Documento() {
    }

    // Getters y Setters
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

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getEmisorYFecha() {
        return emisorYFecha;
    }

    public void setEmisorYFecha(String emisorYFecha) {
        this.emisorYFecha = emisorYFecha;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
}