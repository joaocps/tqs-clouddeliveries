package ua.deti.tqs.projetoapi.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;
import ua.deti.tqs.projetoapi.entities.Comment;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentRepositoryTest {

	@LocalServerPort
    private int port;
	
	@Autowired
	private CommentRep comRep;
	
	@Autowired
	private OrderRep orderRep;
	
	
	private Comment com4;
	
	@Before
    public void setup() {
        comRep.deleteAll();
        orderRep.deleteAll();
        Order order1 = new Order("name");
        Order order2 = new Order("ola");
        
        Comment com1 = new Comment(2, "isto n presta");
        com1.setOrder(order1);
        
        com4 = new Comment(5, "isto é muito bom");
        com4.setOrder(order2);
        System.out.println(com4);
        comRep.saveAll(Arrays.asList(com1, com4));

    }
	
	@Test
	public void getCommentsTest(){
		List<Comment> ret = (List<Comment>) comRep.findBestComments();
		System.out.println(ret);
		Assert.assertEquals(1, ret.size());
		
		Assert.assertEquals(ret.get(0).getText(), com4.getText());
		Assert.assertEquals(ret.get(0).getRating(), com4.getRating());
		Assert.assertEquals(ret.get(0).getOrder().getId(), com4.getOrder().getId());
		
		
	}
	
}
