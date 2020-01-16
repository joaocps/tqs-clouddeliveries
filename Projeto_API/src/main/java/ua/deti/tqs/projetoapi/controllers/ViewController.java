package ua.deti.tqs.projetoapi.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ua.deti.tqs.projetoapi.entities.Comment;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

@Controller

@RequestMapping("/")
public class ViewController {

	@Autowired
	OrderRep orderRep;
	
	@Autowired
	CommentRep comRep;

	@Autowired
	ProductCategoryRep prodRep;
	
	@Autowired
	UserTypeRep userTypeRep;
	
	private static final String REDIRECT = "redirect:";
	private static final String ERRO = "/error";

    private String index = "index";
    private String signup = "sign_up";
    private String login = "login";
    private String chooseService = "choosetype";
    private String userIndex = "user/index";
    private String userProfile = "user/profile";
    private String changeUserProfile = "user/change_profile_info";

    private String home = "home";
    private String about = "about";
    private String serviceInfo = "service_info";

    private String userCreateRequest = "user/makerequest";

    private String errorpage = "error";
    
    
    @PostConstruct
    public void init() {
    	if (prodRep.count() == 0){
    		prodRep.save(new ProductCategory("General Items"));
    		prodRep.save(new ProductCategory("Pianos"));
    		prodRep.save(new ProductCategory("Vehicles"));
    	}
    	if (userTypeRep.count() == 0){
    		userTypeRep.save(new UserType(5, 30));
    		userTypeRep.save(new UserType(50, 250));
    		userTypeRep.save(new UserType(200, 500));
    	}
        

    }

    @GetMapping(value = "index")
    public String returnIndex() {


        return index;
    }

    @GetMapping(value = "sign_up")
    public String returnSign(Model model) {
    	model.addAttribute("user", new User());
        return signup;
    }

    @GetMapping(value = "choosetype")
    public String chooseService(Model model, HttpSession session) {
    	if (session.getAttribute("mayuser") == null)
    		return REDIRECT + ERRO;
        return chooseService;
    }

    @GetMapping(value = "home")
    public String returnHome(Model model) {
    	List<Comment> aux = (List<Comment>) comRep.findBestComments();
    	List<Comment> com = new ArrayList<>();
    	aux.forEach(c -> {
    		if (c.getOrder() != null)
    			com.add(c);
    	});
    	
    	
    	if (!com.isEmpty()){
    		HashMap<Integer, String> names = new HashMap<>();
        	model.addAttribute("comments", com);
        	com.forEach(c -> 
        		names.put(c.getId(), c.getOrder().getUser().getFirstName() + " " + c.getOrder().getUser().getLastName())
        	);
        	model.addAttribute("names", names);
    	}
    	
        return home;
    }

    @GetMapping(value = "about")
    public String returnAbout() {
        return about;
    }

    @GetMapping(value = "service_info")
    public String returnServiceInfo() {
        return serviceInfo;
    }


    @GetMapping(value = "login")
    public String login(Model model) {
    	model.addAttribute("user", new User());
        return login;
    }

    @GetMapping(value = "userhome")
    public ModelAndView userhome(Model model, HttpSession session) {
    	if (session.getAttribute("user") == null){
    		return new ModelAndView(REDIRECT + ERRO);
    	}
    	
    	User user = (User)session.getAttribute("user");
    	model.addAttribute("comment", new Comment());
    	model.addAttribute("orders", orderRep.findOrderByUserId(user.getId()));
    	ModelAndView ret = new ModelAndView();
    	ret.setViewName(userIndex);
    	return ret;
    }

    @GetMapping(value = "userprofile")
    public ModelAndView userprofile(Model model, HttpSession session){
    	if (session.getAttribute("user") == null)
    		return new ModelAndView(REDIRECT + ERRO);
    	
    	User user = (User) session.getAttribute("user");
    	
       
        model.addAttribute("user", user);
        ModelAndView ret = new ModelAndView();
    	ret.setViewName(userProfile);
        return ret;
    }

    @GetMapping(value = "changeuserprofile")
    public ModelAndView changeuserprofile(Model model, HttpSession session){
    	if (session.getAttribute("user") == null)
    		return new ModelAndView(REDIRECT + ERRO);
    	   	
    	User user = (User) session.getAttribute("user");
    	model.addAttribute("user", user);
        return new ModelAndView(changeUserProfile);
    }

    @GetMapping(value = "makeRequest")
    public ModelAndView makeRequest(Model model, HttpSession session) {
    	if (session.getAttribute("user") == null)
    		return new ModelAndView(REDIRECT + ERRO);
    	
    	
    	model.addAttribute("categories", prodRep.findAll());
    	model.addAttribute("order", new Order());
    	ModelAndView ret = new ModelAndView();
    	ret.setViewName(userCreateRequest);
        return ret;
    }

    
    @GetMapping(value = "error")
    public String error() {   	
    	return errorpage;
    }
    
    
}
