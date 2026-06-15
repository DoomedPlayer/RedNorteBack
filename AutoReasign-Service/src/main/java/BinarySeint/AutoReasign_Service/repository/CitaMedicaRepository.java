package BinarySeint.AutoReasign_Service.repository;

import BinarySeint.AutoReasign_Service.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
}
