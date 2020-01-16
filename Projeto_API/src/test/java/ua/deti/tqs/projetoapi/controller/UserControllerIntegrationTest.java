package ua.deti.tqs.projetoapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import ua.deti.tqs.projetoapi.entities.Administrator;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.UserRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;   

    @Autowired
    private UserRep userRep;

    @Autowired
    private ObjectMapper obj;
    
    private User test;
    
    private String address = "Tomar";

    @Before
    public void setup() {
    	userRep.deleteAll();
        test = new User("Diogo", "Costa", "diogocosta");
        test.setAddress(address);
        test.setCountry("Portugal");
        test.setEmail("email@ua.pt");
        test.setPhoneNumber(929184723);
        
        
       
    }
    
    @Test
    public void logoutAdminTest() throws Exception{
    	Administrator admin = new Administrator("test@test.pt", "pass");
    	admin.setToken(999999999);
    	userRep.save(admin);
    	
    	mvc.perform(MockMvcRequestBuilders.delete("/user/admin/logout/" + admin.getEmail())
    			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	Administrator ad = (Administrator) userRep.findAll().get(0);
    	assertEquals(-1, ad.getToken() );
    }
    
    
    @Test
    public void loginAdminTest() throws JsonProcessingException, Exception{
    	Administrator admin = new Administrator("test@test.pt", "pass");
    	userRep.save(admin);
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("email", admin.getEmail());
    	args.put("password", admin.getPassword());
    	
    	mvc.perform(post("/user/admin/login/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(args)))
                .andExpect(status().isOk());
    	
    	assertEquals(1, userRep.count());
    	Administrator aux = (Administrator) userRep.findAll().get(0);
    	assertTrue(aux.getToken() != -1);
    }
    
    @Test
    public void loginAdminTestError() throws JsonProcessingException, Exception{
    	User user = new User("test@test.pt", "pass");
    	userRep.save(user);
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("email", user.getEmail());
    	args.put("password", user.getPassword());
    	
    	mvc.perform(post("/user/admin/login/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(args)))
                .andExpect(status().isBadRequest());
    	
    	assertEquals(1, userRep.count());
    	
    }
    
    @Test
    public void signUpTest() throws JsonProcessingException, Exception{

    	HashMap<String, Object> args = new HashMap<>();
    	args.put("email", test.getEmail());
    	args.put("password", test.getPassword());
    	
    	mvc.perform(post("/user/admin/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(args)))
                .andExpect(status().isOk());
    	
    	assertEquals(1, userRep.count());
    	assertEquals(test.getEmail(), userRep.findAll().get(0).getEmail());
    	assertEquals("Administrator", userRep.findAll().get(0).getClass().getSimpleName());
    }

    
    @Test
    public void processTest() throws Exception {
        RequestBuilder request = post("/user/")
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", test.getAddress())
                .param("country", test.getCountry())
                .param("phoneNumber", Integer.toString(test.getPhoneNumber()));

        HttpSession session = mvc.perform(request).andDo(MockMvcResultHandlers.print()).andReturn().getRequest().getSession();
        assertTrue(session.getAttribute("mayuser").equals(test));

    }

    @Test
    public void verifyTest() throws Exception {
    	userRep.save(test);
    	RequestBuilder request = post("/user/verify_login/")
                .param("email", test.getEmail())
                .param("password", test.getPassword());

        HttpSession session = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print()).andReturn().getRequest().getSession();

        assertEquals(test, session.getAttribute("user"));

    }
    
    @Test
    public void verifyUpdateTest() throws Exception {
    	userRep.save(test);
        
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);
        
        User user = new User(test.getEmail(), test.getPassword());
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setCountry("Country");
        user.setAddress("Aveiro");
        user.setPhoneNumber(99999999);
            	
        RequestBuilder request = post("/user/update/").session(session)
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .param("firstName", user.getFirstName())
                .param("lastName", user.getLastName())
                .param("address", user.getAddress())
                .param("country", user.getCountry())
                .param("phoneNumber", Integer.toString(user.getPhoneNumber()));

        mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        User final_user = userRep.findById(test.getId()).get();
        assertEquals(user.getAddress(), final_user.getAddress());
        System.out.println(final_user.getAddress());
        System.out.println(test.getAddress());
        assertFalse(final_user.getAddress().equals(address));
        assertEquals(user.getPhoneNumber(), final_user.getPhoneNumber());

    }
    
     @Test
    public void verifyUpdateErrorTest() throws Exception {
    	userRep.save(test);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", test);
        
        RequestBuilder request = post("/user/update/").session(session)
                .param("email", test.getEmail())
                .param("password", test.getPassword())
                .param("firstName", test.getFirstName())
                .param("lastName", test.getLastName())
                .param("address", "Aveiro")
                .param("country", test.getCountry())
                .param("phoneNumber", "324f43534");

        mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        User final_user = userRep.findById(test.getId()).get();
        System.out.println(final_user.getAddress());
        
        assertTrue(final_user.getAddress().equals(address));

    }
    
    

}
