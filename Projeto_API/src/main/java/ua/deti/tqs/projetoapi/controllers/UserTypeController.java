package ua.deti.tqs.projetoapi.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

@Controller
@RequestMapping("/usertype")
public class UserTypeController {
	
	@Autowired
	UserTypeRep rep;
	
	@Autowired
	UserRep userRep;
	
	
	
	@GetMapping("/")
	public @ResponseBody List<UserType> get(){
		return rep.findAll();
	}
	
	@GetMapping("/complete")
    public ModelAndView complete(@RequestParam("id") int id, HttpSession session){
    	ModelAndView ret;
    	if (session.getAttribute("mayuser") == null){
    		ret = new ModelAndView("redirect:../error");
    		return ret;
    	}
    	User user = (User) session.getAttribute("mayuser");
    	List<UserType> list = rep.findAll();
    	
    	Collections.sort(list, new Comparator<UserType>() {
			@Override
			public int compare(UserType arg0, UserType arg1) {
				if (arg0.getNumOrders() < arg1.getNumOrders())
					return -1;
				return 1;
			}
    	});
    	switch(id){
    	case 0:
    		user.setType(list.get(0));
    		break;
    	case 1:
    		user.setType(list.get(1));
    		break;
    	case 2:
    		user.setType(list.get(2));
    		break;
    	default:
    		return new ModelAndView("redirect:../error");
    	}
    	
    	userRep.save(user);
    	ret = new ModelAndView("redirect:../login");
    	return ret;
    	
    }

}
