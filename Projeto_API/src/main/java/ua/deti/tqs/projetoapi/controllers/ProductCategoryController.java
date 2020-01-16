package ua.deti.tqs.projetoapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;

@Controller
@RequestMapping("/categorie")
public class ProductCategoryController {
	
	@Autowired
	public ProductCategoryRep rep;
	
	@GetMapping("/")
	public @ResponseBody Iterable<ProductCategory> get(){
		return rep.findAll();
	}
	
	

}
