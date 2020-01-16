package ua.deti.tqs.projetoapi.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.deti.tqs.projetoapi.entities.Driver;

@Repository
public interface DriverRep extends JpaRepository<Driver, Integer>{
    
    public Optional<Driver> findByContact(int contact);

}
