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
    private String nombreCompleto; // Pulimos el dato: juntaremos nombre y apellido aquí
    private String correo;
    private Boolean alertasActivas;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaParentesco;
    private String contactoEmergenciaTelefono;
    
    // Este campo es clave para el Caso RedNorte. 
    // Por ahora diremos que está "En Evaluación", más adelante el BFF le preguntará al Waitlist-Service.
    private String estadoListaEspera; 
}