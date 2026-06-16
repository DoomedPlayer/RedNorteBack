package com.rednorte.portal.dtos;

import lombok.Data;

@Data
public class PacienteRequestDTO {
    private String rut;
    private String primerNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String antecedentesMedicos;
}