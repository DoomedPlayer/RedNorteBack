package BinarySeint.Waitlist_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_espera")
public class RegistroEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12)
    private String rutPaciente;

    @Column(nullable = false)
    private String especialidad;

    @Column(nullable = false)
    private String tipoAtencion; // EJ: CONSULTA, CIRUGIA, URGENCIA

    @Column(nullable = false)
    private Integer nivelPrioridad; // 1 (Urgencia Vital) a 5 (Consulta General)

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(nullable = false)
    private String estado; // EN_ESPERA, ASIGNADO, CANCELADO

    // Constructores, Getters y Setters (Puedes omitir esto si usas Lombok con @Data)
    
    public RegistroEspera() {}

    // Getters y Setters...
}