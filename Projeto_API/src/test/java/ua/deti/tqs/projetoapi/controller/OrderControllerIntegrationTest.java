package ua.deti.tqs.projetoapi.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.deti.tqs.projetoapi.entities.CommentTest;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.OrderTest;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper obj;

    @Autowired
    private UserRep userRep;

    @Autowired
    private OrderRep orderRep;

    @Autowired
    private ProductCategoryRep catRep;
    
    @Autowired
    private UserTypeRep typeRep;

    @Autowired
    private CommentRep comRep;
    
    MockHttpSession session;

    CommentTest test;

    Order order;
    
    ProductCategory cat;
    
    User user;

    @Before
    public void setup() {
        userRep.deleteAll();
        orderRep.deleteAll();
        catRep.deleteAll();
        comRep.deleteAll();
        typeRep.deleteAll();

        
        UserType type = new UserType(2, 10);
        
        user = new User("test@ua.pt", "test");
        user.setType(type);
        
        cat = new ProductCategory("category");
        
        order = new Order("test1", cat, user);
        
        test = new CommentTest(order.getId(), 5, "test test");
        
        typeRep.saveAndFlush(type);
        orderRep.save(order);
        userRep.save(user);
        catRep.save(cat);
        

    }
    
    
    @Test
    public void postOrderTest() throws Exception {
        OrderTest order = new OrderTest();
        order.setAddress("Tomar");
        order.setName("Piano");
        order.setRecipientContact("999999999");
        order.setCategory(Integer.toString(cat.getId()));
        
        session = new MockHttpSession();
        session.setAttribute("user", user);
        
        
        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)))
                .andExpect(content().string("success"));
    }
    
    

    @Test
    public void postTwoOrdersTest() throws Exception {
        OrderTest order = new OrderTest();
        order.setAddress("Tomar");
        order.setName("Piano");
        order.setRecipientContact("999999999");
        order.setCategory(Integer.toString(cat.getId()));

        session = new MockHttpSession();
        session.setAttribute("user", user);
        
        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)))
                .andExpect(content().string("success"));

        OrderTest order2 = new OrderTest();
        order2.setAddress("Anadia");
        order2.setName("Cavalo");
        order2.setRecipientContact("666666666");
        order2.setCategory(Integer.toString(cat.getId()));
        

        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order2)))
                .andExpect(content().string("success"));
    }

    @Test
    public void postIncorrectOrderTest() throws Exception {
        OrderTest order = new OrderTest();
        order.setAddress("Porto");
        order.setName("Pónei");
        order.setRecipientContact("9999a9999");
        order.setCategory(Integer.toString(cat.getId()));
        
        session = new MockHttpSession();
        session.setAttribute("user", user);

        mvc.perform(post("/order/").session(session)
               .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)))
                .andExpect(content().string("fail"));
    }

    @Test
    public void postIncorrectOrderAndCorrectOrderTest() throws Exception {
        OrderTest order = new OrderTest();
        order.setAddress("Evora");
        order.setName("Javali");
        order.setRecipientContact("999/99999");
        order.setCategory(Integer.toString(cat.getId()));
        
        session = new MockHttpSession();
        session.setAttribute("user", user);
        
        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)))
                .andExpect(content().string("fail"));

        OrderTest order2 = new OrderTest();
        order2.setAddress("Evora");
        order2.setName("Javali");
        order2.setRecipientContact("999999999");
        order2.setCategory(Integer.toString(cat.getId()));
        

        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order2)))
                .andExpect(content().string("success"));

    }

    @Test
    public void postCorrectOrderAndIncorrectOrderTest() throws Exception {

        OrderTest order = new OrderTest();
        order.setAddress("Evora");
        order.setName("Javali");
        order.setRecipientContact("999999999");
        order.setCategory(Integer.toString(cat.getId()));
        
        session = new MockHttpSession();
        session.setAttribute("user", user);
        
        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order)))
                .andExpect(content().string("success"));
        
        OrderTest order2 = new OrderTest();
        order2.setAddress("Evora");
        order2.setName("Javali");
        order2.setRecipientContact("999/99999");
        order2.setCategory(Integer.toString(cat.getId()));
        

        mvc.perform(post("/order/").session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(order2)))
                .andExpect(content().string("fail"));

    }
    
    

    @Test
    public void postComment() throws Exception {
    	CommentTest test = new CommentTest(order.getId(), 4, "merda");
    	
    	
        mvc.perform(post("/order/comment/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(test)))
                .andExpect(content().string("success"));

        System.out.println(comRep.findAll());
        
        assertEquals(1, comRep.count());
        assertEquals(order.getId(), comRep.findAll().get(0).getOrder().getId());
        assertEquals(test.getText(),comRep.findAll().get(0).getText());
    }
    
    

    public String asJsonString(final Object o) {
        try {
            return obj.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
