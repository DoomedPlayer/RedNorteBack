package BinarySeint.Waitlist_Service.service;

import BinarySeint.Waitlist_Service.factory.WaitlistFactoryMethod;
import BinarySeint.Waitlist_Service.factory.WaitlistFactoryProvider;
import BinarySeint.Waitlist_Service.model.EstadoPaciente;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import BinarySeint.Waitlist_Service.repository.ListaEsperaRepository;
import BinarySeint.Waitlist_Service.repository.RegistroPacienteRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WaitlistService {

    @Autowired
    private ListaEsperaRepository listaEsperaRepo;

    @Autowired
    private RegistroPacienteRepository registroPacienteRepo;

    @Autowired
    private WaitlistFactoryProvider factoryProvider;


   @Transactional
    public RegistroPaciente guardarRegistroPaciente(String rut, EstadoPaciente estado, String prioridad) {
        RegistroPaciente paciente = registroPacienteRepo.findById(rut).orElse(new RegistroPaciente());
        paciente.setRutPaciente(rut);
        paciente.setEstado(estado); // Usamos el Enum
        paciente.setFechaRegistro(LocalDate.now());
        paciente.setPrioridad(prioridad);
        
        return registroPacienteRepo.save(paciente);
    }

    public RegistroPaciente obtenerRegistroPorRut(String rutPaciente) {
        return registroPacienteRepo.findById(rutPaciente).orElse(null); 
    }

    @Transactional
    public RegistroPaciente modificarRegistroPorDoctor(String rut, EstadoPaciente nuevoEstado, String nuevaPrioridad) {
        RegistroPaciente paciente = registroPacienteRepo.findById(rut)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con RUT: " + rut));
        
        paciente.setEstado(nuevoEstado);
        
        if (nuevaPrioridad != null && !nuevaPrioridad.isEmpty()) {
            paciente.setPrioridad(nuevaPrioridad);
        }

        paciente.setFechaRegistro(LocalDate.now()); 
        
        return registroPacienteRepo.save(paciente);
    }

    @Transactional
    public RegistroPaciente registrarEnListaEspera(String rut, Integer especialidad, String tipoAtencion) {

        WaitlistFactoryMethod fabrica = factoryProvider.obtenerFabrica(tipoAtencion);
        ListaEspera ticket = fabrica.crearRegistro(rut, especialidad );
        listaEsperaRepo.save(ticket);
        String prioridadTexto = traducirPrioridad(ticket.getNivelPrioridad());
        
        return guardarRegistroPaciente(rut, EstadoPaciente.EN_ESPERA, prioridadTexto);
    }

    public List<ListaEspera> obtenerListaPorEspecialidad(Integer especialidad) {
        return listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(especialidad);
    }

    @Transactional
    public boolean eliminarRegistroPorRut(String rutPaciente) {
        try {
            listaEsperaRepo.deleteByRutPaciente(rutPaciente); 

            guardarRegistroPaciente(rutPaciente, EstadoPaciente.HORA_ASIGNADA, "-");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar de lista de espera: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public String obtenerYSacarSiguientePaciente(String nombreEspecialidad) {
        Integer idEspecialidad = 5; 
        if (nombreEspecialidad.contains("Urgencia")) idEspecialidad = 1;
        else if (nombreEspecialidad.contains("Cirugía")) idEspecialidad = 2;
        else if (nombreEspecialidad.contains("Procedimiento")) idEspecialidad = 3;

        List<ListaEspera> fila = listaEsperaRepo.findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(idEspecialidad);
        
        if (fila.isEmpty()) {
            return null; 
        }

        ListaEspera siguientePaciente = fila.get(0);
        String rutSiguiente = siguientePaciente.getRutPaciente();

        eliminarRegistroPorRut(rutSiguiente);

        return rutSiguiente;
    }

    private String traducirPrioridad(Integer nivelPrioridad) {
        switch(nivelPrioridad) {
            case 1: return "Nivel 1 - Urgencia";
            case 2: return "Nivel 2 - Cirugía";
            case 3: return "Nivel 3 - Procedimiento";
            default: return "Nivel 5 - Consulta General";
        }
    }
}