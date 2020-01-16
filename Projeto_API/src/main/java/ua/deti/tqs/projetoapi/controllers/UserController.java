package ua.deti.tqs.projetoapi.controllers;

import java.util.Map;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ua.deti.tqs.projetoapi.entities.Administrator;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.UserRep;

@Controller
@RequestMapping(path = "/user")
public class UserController{
	

	private static final  String ERROR = "ERROR! :";
    private static final  Logger LOGGER = Logger.getLogger(UserController.class);
    
    private static final String REDIRECT = "redirect:";
    private static final String ERRO = "../error";


    @Autowired
    UserRep userRep;
    
    
    
    public int generateToken(){
    	int min = 10000000;
    	int max = 99999999;
    	
    	return new Random().nextInt(max - min) + min;
    }
    
    
    
    @GetMapping(value = "/delete")
    public void delete(){
    	userRep.deleteAll();
    }


    
    @GetMapping(value = "/")
    public @ResponseBody
    Iterable<User> getUsers() {
        return userRep.findAll();

    }

    
    @GetMapping(value = "/{id}")
    public @ResponseBody
    Optional<User> getUser(@PathVariable int id) {
    	return userRep.findById(id);

        

    }
    
    @DeleteMapping("admin/logout/{email}")
    public ResponseEntity logoutAdmin(@PathVariable("email") String email){
    	Optional<User> user = userRep.findByEmail(email);
    	if (user.isPresent() && user.get() instanceof Administrator){
    		Administrator ad = (Administrator) user.get();
    		ad.setToken(-1);
    		userRep.save(ad);
    		return new ResponseEntity(HttpStatus.OK);
    	}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }
    
    @PostMapping("/admin/login")
    public ResponseEntity loginAdmin(@RequestBody Map<String, Object> args){
    	try {
    		String email = args.get("email").toString();
    		String password = args.get("password").toString();
    		Optional<User> admin = userRep.findByEmail(email);
    		if (admin.isPresent() && admin.get() instanceof Administrator){
    			
    			int token = generateToken();
    			Administrator ad = (Administrator) admin.get();
    			if (ad.getPassword().equals(password)){
    				ad.setToken(token);
        			userRep.save(ad);
            		return new ResponseEntity(token, HttpStatus.OK);
    			}
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    		
    		return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	} catch(Exception e){
    		LOGGER.error(ERROR + e.getMessage());
    		return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @PostMapping(path = "/admin")
    public ResponseEntity signupAdmin(@RequestBody Map<String, Object> args){
    	try {
    		String email = args.get("email").toString();
    		String password = args.get("password").toString();
    		Administrator admin = new Administrator(email, password);
    		userRep.save(admin);
    		return new ResponseEntity(HttpStatus.OK);
    	} catch(Exception e){
    		LOGGER.error(ERROR + e.getMessage());
    		return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	}
    }

    @PostMapping(path = "/")
    public ModelAndView signup(@ModelAttribute("User") @Validated User user, BindingResult result, HttpSession session) {
        ModelAndView ret;

        if (result.hasErrors()) {
            ret = new ModelAndView(REDIRECT + ERRO);
            return ret;
        }
        ret = new ModelAndView(REDIRECT + "../choosetype");

        session.setAttribute("mayuser", user);
        return ret;
    }
    
    

    @PostMapping(path = "/update")
    public ModelAndView processUpdate(@ModelAttribute("User") @Validated User user, BindingResult result, HttpSession session) {
        ModelAndView ret;
        if (result.hasErrors()) {
            ret = new ModelAndView(REDIRECT + ERRO);
            return ret;
        }
        ret = new ModelAndView(REDIRECT + "../userhome");
        User u = (User) session.getAttribute("user");
        user.setId(u.getId());
        userRep.save(user);
        session.setAttribute("user", user);
        return ret;
    }

    @PostMapping(path = "/verify_login")
    public ModelAndView login(@ModelAttribute("User") @Validated User user, HttpSession session) {
        ModelAndView ret;
        Optional<User> u = userRep.findByEmail(user.getEmail());

        if (!u.isPresent()) {
            ret = new ModelAndView(REDIRECT + ERRO);
            return ret;
        }

        if (!user.getPassword().equals(u.get().getPassword())) {
            ret = new ModelAndView(REDIRECT + ERRO);
            return ret;
        }
        
        session.setAttribute("user", u.get());
        
        ret = new ModelAndView(REDIRECT + "../userhome");
        return ret;

    }

    @GetMapping(path = "/logout")
    public ModelAndView logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        
        return new ModelAndView(REDIRECT + "../home");
    }

}
