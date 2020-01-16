package ua.deti.tqs.projetoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.deti.tqs.projetoapi.entities.Order;

@Repository
public interface OrderRep extends JpaRepository<Order, Integer>{
	
	@Query(value = "SELECT * from orders WHERE orders.user_id = :id", nativeQuery = true)
	public Iterable<Order> findOrderByUserId(@Param("id") int id );
	
	
	
}
