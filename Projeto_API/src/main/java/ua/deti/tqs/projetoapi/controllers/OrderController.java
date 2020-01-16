package ua.deti.tqs.projetoapi.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.deti.tqs.projetoapi.entities.Comment;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.State;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;

import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.VeichleRep;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
	
	private static final  String ERROR = "ERROR! :";
    private static final  Logger LOGGER = Logger.getLogger(OrderController.class);
	
	@Autowired
	private OrderRep orderRep;
	
	@Autowired
	private UserRep userRep;
	
	@Autowired
	private VeichleRep vRep;
	
	@Autowired
	private ProductCategoryRep categorieRep;
	
	@Autowired
	private CommentRep comRep;
	

	@Autowired
	ObjectMapper mapper;
	
	
	
	@GetMapping(value = "/")
	public Iterable<Order> getOrders() {
		return orderRep.findAll();

	}

	
	@GetMapping(value = "/user/{email}")
	public ResponseEntity getOrdersUser(@PathVariable String email) {
		Optional<User> user = userRep.findByEmail(email);
		if (user.isPresent()){
			User u = user.get();
			Iterable<Order> orders = orderRep.findOrderByUserId(u.getId());
			return new ResponseEntity(orders, HttpStatus.OK);
		}
		
		return new ResponseEntity(HttpStatus.BAD_REQUEST);

	}
	
	
	@GetMapping(value = "/{idUser}")
	public Iterable<Order> getOrdersUser(@PathVariable int idUser) {
		return orderRep.findOrderByUserId(idUser);

	}
	
	@PostMapping(value = "/comment", consumes = "application/json")
	public String comment(@RequestBody Map<String, Object> arg){
		String text = arg.get("text").toString();
		int orderId = Integer.parseInt(arg.get("orderId").toString());

		int rating = Integer.parseInt(arg.get("rating").toString());
		Comment com = new Comment(rating, text);

		Optional<Order> aux = orderRep.findById(orderId);
		if (aux.isPresent()){
			
			Order order = aux.get();
			com.setOrder(order);
			comRep.saveAndFlush(com);
			
		}
		
		return "success";
	}
	
	public boolean isInt(String str){
		try{
			Integer.parseInt(str);
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	@PutMapping(value = "/state/{id}/{state}")
	public ResponseEntity updateState(@PathVariable("id") int id, @PathVariable("state") String state){
		try {
			Optional<Order> aux = orderRep.findById(id);
			if (aux.isPresent()){
				Order order = aux.get();
				switch(state){
				case "progress":
					order.setState(State.IN_PROGRESS);
					break;
				case "deliver":
					order.setState(State.DELIVER);
					break;
				case "finish":
					order.setState(State.FINISH);
					break;
				default:
					LOGGER.error(ERROR + " - State undefined");
				}
				orderRep.saveAndFlush(order);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch(Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
	} 
	
	@PostMapping(value = "/", consumes = "application/json")
	public String postOrder(@RequestBody Map<String, Object> arg, HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user.canMakeRequest() && isInt(arg.get("recipientContact").toString())){
			@Valid
			Order order = mapper.convertValue(arg, Order.class);
			int id = Integer.parseInt(arg.get("category").toString());
                        Optional <ProductCategory> aux = categorieRep.findById(id);
			if (aux.isPresent()){
				ProductCategory cat = aux.get();
				order.setCategory(cat);
				order.setUser(user);
				if (vRep.count() > 0){
					int max = (int) (vRep.count() - 1);
					int range = max + 1;
					
					int rand = new Random().nextInt(range);
					order.setVeichle(vRep.findAll().get(rand));
				}
				order.setState(State.CREATE);
				user.addOrder(order);
				
				orderRep.saveAndFlush(order);
				return "success";
			}
			return "fail";

		}
		return "fail";
		
	}
	
	@GetMapping(value = "/delete")
	public void deleteOrders(){
		orderRep.deleteAll();
	}


}
