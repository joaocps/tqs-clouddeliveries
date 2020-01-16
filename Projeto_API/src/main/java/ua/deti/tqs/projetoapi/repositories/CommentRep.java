package ua.deti.tqs.projetoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.deti.tqs.projetoapi.entities.Comment;

@Repository
public interface CommentRep extends JpaRepository<Comment, Integer>{
	
	@Query(value = "select * from comment where comment.rating >= 4;", nativeQuery = true)
	public Iterable<Comment> findBestComments();
	
}
