package ua.deti.tqs.projetoapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.deti.tqs.projetoapi.entities.User;

@Repository
public interface UserRep extends JpaRepository<User, Integer>{
	
	public Optional<User> findByEmail(String email);

}
