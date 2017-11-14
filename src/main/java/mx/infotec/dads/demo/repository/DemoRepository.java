package mx.infotec.dads.demo.repository;

import mx.infotec.dads.demo.domain.Demo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Demo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemoRepository extends JpaRepository<Demo,Long> {

}
