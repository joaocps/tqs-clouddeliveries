package ua.deti.tqs.projetoapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.deti.tqs.projetoapi.entities.Comment;
import ua.deti.tqs.projetoapi.repositories.CommentRep;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	CommentRep comRep;
	
	@GetMapping("/")
	public @ResponseBody List<Comment> getComments(){
		return comRep.findAll();
	}
	
	@GetMapping("/delete")
	public @ResponseBody String deleteComments(){
		comRep.deleteAll();
		return "success";
	}
	
}
