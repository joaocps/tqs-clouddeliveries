/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.projetoapi.entities.Driver;
import ua.deti.tqs.projetoapi.repositories.DriverRep;
import ua.deti.tqs.projetoapi.repositories.VeichleRep;

/**
 *
 * @author jcps
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class DriverControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRep driverRep;
    
    @Autowired
    private VeichleRep vRep;

    private Driver driverTest;

    
    @Autowired
    ObjectMapper obj;

    @Before
    public void setup() {
        
        vRep.deleteAll();
        driverRep.deleteAll();
        driverTest = new Driver(666666661);
        
    }
    
    @Test
    public void postTest() throws Exception {
        
        mvc.perform(post("/driver/").contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(driverTest)))
                .andExpect(status().isOk());
               
        
        Assert.assertTrue(driverRep.count() == 1);
        Assert.assertEquals(driverRep.findAll().get(0).getContact(), driverTest.getContact());
        
    }
    
    @Test
    public void deleteTest() throws Exception {
        
        driverRep.save(driverTest);
        
        mvc.perform(MockMvcRequestBuilders.delete("/driver/" + driverTest.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        Assert.assertEquals(0, driverRep.count());        
        
    }
    
    @Test
    public void postAndDeleteTest() throws Exception {

        mvc.perform(post("/driver/").contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(driverTest)))
                .andExpect(status().isOk());
        
        Assert.assertTrue(driverRep.count() == 1);
        Assert.assertEquals(driverRep.findAll().get(0).getContact(), driverTest.getContact());
        
        Optional<Driver> d = driverRep.findByContact(driverTest.getContact());
        int id = d.get().getId();
        
        mvc.perform(MockMvcRequestBuilders.delete("/driver/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        Assert.assertEquals(0, driverRep.count());
                
    }
    
    @Test
    public void failPostTest() throws Exception {
        
        HashMap<String, Object> test = new HashMap<>();
        test.put("contact", "98798s");
     
        mvc.perform(post("/driver/").contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(test)))
                .andExpect(status().isBadRequest());
        
        Assert.assertTrue(driverRep.count() == 0);
    }
    
    @Test
    public void putTest() throws JsonProcessingException, Exception{
    	driverRep.save(driverTest);
    	int newContact = 999999999;
    	
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("contact", newContact);
    	
    	mvc.perform(put("/driver/" + driverRep.findAll().get(0).getId()).contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(args)))
                .andExpect(status().isOk());
    
    	Assert.assertEquals(newContact, driverRep.findAll().get(0).getContact());
    	
    }
    
    @Test
    public void putTestError() throws JsonProcessingException, Exception{
    	driverRep.save(driverTest);
    	String newContact = "aass9823";
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("contact", newContact);
    	
    	mvc.perform(put("/driver/" + driverRep.findAll().get(0).getId()).contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(args)))
                .andExpect(status().isBadRequest());
    
    	Assert.assertEquals(666666661, driverRep.findAll().get(0).getContact());
    	
    }
    
    
    

    
    

}
