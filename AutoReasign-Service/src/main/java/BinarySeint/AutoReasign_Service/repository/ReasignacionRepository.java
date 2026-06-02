package BinarySeint.AutoReasign_Service.repository;

import BinarySeint.AutoReasign_Service.model.Reasignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasignacionRepository extends JpaRepository<Reasignacion, Long> {

}
