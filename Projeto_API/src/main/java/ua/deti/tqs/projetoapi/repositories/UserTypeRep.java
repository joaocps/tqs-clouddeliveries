package ua.deti.tqs.projetoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.deti.tqs.projetoapi.entities.UserType;

@Repository
public interface UserTypeRep extends JpaRepository<UserType, Integer>{

}
