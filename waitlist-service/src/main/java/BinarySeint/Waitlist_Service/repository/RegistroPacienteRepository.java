package BinarySeint.Waitlist_Service.repository;

import BinarySeint.Waitlist_Service.model.RegistroPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroPacienteRepository extends JpaRepository<RegistroPaciente, String> {
}