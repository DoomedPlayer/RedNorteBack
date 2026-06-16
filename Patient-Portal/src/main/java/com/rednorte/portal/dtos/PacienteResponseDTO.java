package com.rednorte.portal.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteResponseDTO {
    private String rut;
    private String nombre; // Uniremos nombre y apellido aquí
    private Integer edad; 
    private String prevision; 
    private String estado; 
}