package ua.deti.tqs.projetoapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import ua.deti.tqs.projetoapi.controllers.OrderController;
import ua.deti.tqs.projetoapi.entities.CommentTest;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.VeichleRep;


@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRep orderRep;
    
    @MockBean
    private UserRep userRep;
    
    @MockBean
    private CommentRep comRep;
    
    @MockBean
    private VeichleRep vRep;

    @MockBean
    private ProductCategoryRep categoryRep;
    

    @Autowired
    ObjectMapper obj;
    
   

    @Test
    public void getOrdersTest() throws Exception {
    	Order order = new Order("order1");
    	Order order2 = new Order("order2");

        List<Order> allOrders = Arrays.asList(order, order2);

        given(orderRep.findAll()).willReturn(allOrders);

        mvc.perform(get("/order/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(order.getName())))
                .andExpect(jsonPath("$[1].name", is(order2.getName())));
    }

    @Test
    public void getOrders_UserTest() throws Exception{
    	User alex = new User("Alex", "Silva", "123456");
    	User lucas = new User("Lucas", "Barros", "sporting");
    	
    	Order order = new Order("my books", lucas);
    	
    	List<Order> lucasOrders = Arrays.asList(order);

        given(orderRep.findOrderByUserId(lucas.getId())).willReturn(lucasOrders);

        mvc.perform(get("/order/" + lucas.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(order.getName())));
    	

    }
   
   

    @Test
    public void postComment() throws Exception {
        User user = new User("test@ua.pt", "test");
        ProductCategory cat = new ProductCategory("category");
        Order order = new Order("test1", cat, user);
        CommentTest test = new CommentTest(order.getId(), 5, "test test");
        given(orderRep.findById(order.getId())).willReturn(Optional.of(order));

        mvc.perform(post("/order/comment/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(test)))
                .andExpect(content().string("success"));

    }

    public String asJsonString(final Object o) {
        try {
            return obj.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
