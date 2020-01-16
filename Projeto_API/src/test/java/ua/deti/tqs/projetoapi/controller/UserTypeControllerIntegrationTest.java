package ua.deti.tqs.projetoapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;

import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserTypeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRep userRep;

    @Autowired
    private UserTypeRep userTypeRep;   

    private User user;

    @Before
    public void setup() {
        userRep.deleteAll();
        userTypeRep.deleteAll();
        user = new User("Test@ua.pt", "test");
        //userRep.save(user);
        userTypeRep.save(new UserType(3, 30));
        userTypeRep.save(new UserType(5, 50));
        userTypeRep.save(new UserType(4, 40));
        

    }

    @Test
    public void completeTest() throws Exception {
    	MockHttpSession session = new MockHttpSession();
        session.setAttribute("mayuser", user);
    	
        mvc.perform(get("/usertype/complete?id=0")
                .contentType(MediaType.APPLICATION_JSON).session(session))
        		
                .andExpect(view().name("redirect:../login"));

        Assert.assertTrue(userRep.findByEmail("Test@ua.pt").isPresent());
        Assert.assertEquals(get_lower(), userRep.findByEmail("Test@ua.pt").get().getType());

    }

    
    @Test
    public void completeErrorTest() throws Exception {
    	MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        
        mvc.perform(get("/usertype/complete?id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(view().name("redirect:../error"));

        Assert.assertFalse(userRep.findById(user.getId()).isPresent());

    }
    

    @After
    public void tearDown() {
    	userRep.deleteAll();
        userTypeRep.deleteAll();
    }

    public UserType get_lower() {
        List<UserType> list = userTypeRep.findAll();
        UserType menor = list.get(0);
        for (UserType type : list) {
            if (type.getNumOrders() < menor.getNumOrders()) {
                menor = type;
            }
        }
        return menor;
    }

}
