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

    @PostMapping("/registrar")
    public ResponseEntity<RegistroEspera> registrarPaciente(@RequestBody Map<String, Object> request) {
        // Parseamos el dato a Integer para pasarlo al servicio
        Integer idEspecialidad = Integer.parseInt(request.get("idEspecialidad").toString());
        
        RegistroEspera registrado = service.registrarPaciente(
                request.get("rutPaciente").toString(),
                idEspecialidad,
                request.get("tipoAtencion").toString()
        );
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    // Ahora la URL recibe un número de especialidad: GET /api/espera/lista/1
    @GetMapping("/lista/{idEspecialidad}")
    public ResponseEntity<List<RegistroEspera>> obtenerLista(@PathVariable Integer idEspecialidad) {
        List<RegistroEspera> lista = service.obtenerListaPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(lista);
    }
}