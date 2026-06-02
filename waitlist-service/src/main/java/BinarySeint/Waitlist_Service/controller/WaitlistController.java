package BinarySeint.Waitlist_Service.controller;

import BinarySeint.Waitlist_Service.model.RegistroEspera;
import BinarySeint.Waitlist_Service.service.WaitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/espera")
public class WaitlistController {

    @Autowired
    private WaitlistService service;

    // Endpoint para que el Frontend registre un paciente: POST /api/espera/registrar
    @PostMapping("/registrar")
    public ResponseEntity<RegistroEspera> registrarPaciente(@RequestBody Map<String, String> request) {
        RegistroEspera registrado = service.registrarPaciente(
                request.get("rut"),
                request.get("especialidad"),
                request.get("tipoAtencion")
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    // Endpoint para ver la lista de una especialidad: GET /api/espera/lista/{especialidad}
    @GetMapping("/lista/{especialidad}")
    public ResponseEntity<List<RegistroEspera>> obtenerLista(@PathVariable String especialidad) {
        List<RegistroEspera> lista = service.obtenerListaPorEspecialidad(especialidad);
        return ResponseEntity.ok(lista);
    }
}