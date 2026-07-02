package BinarySeint.BFF.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private String rut;
    private String nombreCompleto;
    private String correo;
    private Boolean alertasActivas;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaParentesco;
    private String contactoEmergenciaTelefono;
    private String estadoListaEspera; 
    private Integer edad;
    private String prevision;
    private Boolean esGes;
    private String antecedentesMedicos;
}