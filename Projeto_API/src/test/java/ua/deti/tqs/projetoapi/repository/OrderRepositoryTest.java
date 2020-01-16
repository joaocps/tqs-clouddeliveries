package ua.deti.tqs.projetoapi.repository;

import org.junit.runner.RunWith;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")

public class OrderRepositoryTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OrderRep orderRep;

    @Autowired
    private UserRep userRep;

    private User lucas;
    private User alex;

    private Order order;
    private Order order2;

    @Before
    public void setup() {
        orderRep.deleteAll();
        userRep.deleteAll();
        this.alex = new User("Alex", "Silva", "123456");
        this.lucas = new User("Lucas", "Barros", "sporting");
        this.order = new Order("my books", lucas);
        this.order2 = new Order("my films", alex);

        userRep.save(lucas);
        userRep.save(alex);

        orderRep.save(order);
        orderRep.save(order2);

    }

    @Test
    public void Test() {
        System.out.println(lucas);
        List<Order> res = (List<Order>) orderRep.findOrderByUserId(lucas.getId());

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(order.getName(), res.get(0).getName());

    }

    @After
    public void clean() {
        orderRep.deleteAll();
        userRep.deleteAll();
    }

}
