package br.com.compass.challenge2.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.compass.challenge2.entity.Squad;
@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {	

}
