package com.rednorte.portal.dtos;

public class ActualizarDatosDTO {
    
}
package com.rednorte.portal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarDatosDTO {
    private String correo;
    private String telefono;
    private String direccion;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
}