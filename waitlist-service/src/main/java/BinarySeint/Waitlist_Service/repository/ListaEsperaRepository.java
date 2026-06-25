package BinarySeint.Waitlist_Service.repository;

import BinarySeint.Waitlist_Service.model.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {

    List<ListaEspera> findByIdEspecialidadOrderByNivelPrioridadAscFechaIngresoAsc(Integer idEspecialidad);

    void deleteByRutPaciente(String rutPaciente);
}
