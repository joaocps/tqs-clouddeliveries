package ua.deti.tqs.projetoapi.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;

import ua.deti.tqs.projetoapi.entities.Driver;
import ua.deti.tqs.projetoapi.entities.Veichle;
import ua.deti.tqs.projetoapi.entities.VeichleType;
import ua.deti.tqs.projetoapi.repositories.DriverRep;
import ua.deti.tqs.projetoapi.repositories.VeichleRep;
import ua.deti.tqs.projetoapi.repositories.VeichleTypeRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class VeichleControllerIntegrationTest {

	@LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    VeichleRep vRep;
    
    @Autowired
    DriverRep dRep;
    
    @Autowired
    VeichleTypeRep vtRep;
    
    Driver driver;
    
    VeichleType vt;
    
    @Before
    public void setup(){
    	vRep.deleteAll();
    	dRep.deleteAll();
    	vtRep.deleteAll();
    	
    	driver = new Driver(999999999);
    	vt = new VeichleType("camiao", 500);
    	
    	dRep.saveAndFlush(driver);
    	vtRep.saveAndFlush(vt);
    	
    }
    
    @Test
    public void postVeichleTest() throws Exception{
    	
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("id_veichle_type", vtRep.findAll().get(0).getId());
    	args.put("id_driver", dRep.findAll().get(0).getId());
    	
    	mvc.perform(post("/veichle/")
    			.content(objectMapper.writeValueAsString(args))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(1, vRep.count());
    	assertEquals(driver.getId(), vRep.findAll().get(0).getDriver().getId());
    	
    	
    }
    
    @Test
    public void deleteVeichleTest() throws Exception{
    	Veichle type = new Veichle();
    	vRep.save(type);
    	
    	mvc.perform(MockMvcRequestBuilders.delete("/veichle/" + type.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(0, vRep.count());
    	
    }
    
    @Test
    public void putVeichleTest() throws JsonProcessingException, Exception{
    	Veichle type = new Veichle(vt, driver);
    	vRep.save(type);
    	Driver d = new Driver(888888888);
    	dRep.save(d);
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("id_veichle_type", vt.getId());
    	args.put("id_driver", d.getId());
    	
    	mvc.perform(put("/veichle/" + vRep.findAll().get(0).getId())
    			.content(objectMapper.writeValueAsString(args))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(1, vRep.count());
    	assertEquals(d.getId(), vRep.findAll().get(0).getDriver().getId());
    }
    
    @After
    public void tearDown() throws Exception{
        dRep.deleteAll();
    }
	
}
