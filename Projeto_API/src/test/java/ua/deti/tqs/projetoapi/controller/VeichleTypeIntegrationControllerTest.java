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

import ua.deti.tqs.projetoapi.entities.VeichleType;
import ua.deti.tqs.projetoapi.repositories.VeichleTypeRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class VeichleTypeIntegrationControllerTest {

	@LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    VeichleTypeRep vTypeRep;
    
    @Before
    public void setup(){
    	vTypeRep.deleteAll();
    }
    
    @Test
    public void postVeichleTypeTest() throws Exception{
    	VeichleType type = new VeichleType("test", 10);
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("name", type.getName());
    	args.put("capacity", type.getCapacity());
    	
    	mvc.perform(post("/typeveichle/")
    			.content(objectMapper.writeValueAsString(args))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(1, vTypeRep.count());
    	assertEquals(type.getName(), vTypeRep.findAll().get(0).getName());
    	
    	
    }
    
    @Test
    public void deleteVeichleTest() throws Exception{
    	VeichleType type = new VeichleType("test", 10);
    	vTypeRep.save(type);
    	
    	mvc.perform(MockMvcRequestBuilders.delete("/typeveichle/" + type.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(0, vTypeRep.count());
    	
    }
    
    @Test
    public void putVeichleTest() throws JsonProcessingException, Exception{
    	VeichleType type = new VeichleType("test", 10);
    	vTypeRep.save(type);
    	
    	HashMap<String, Object> args = new HashMap<>();
    	args.put("name", "new name");
    	args.put("capacity", 12.9);
    	
    	mvc.perform(put("/typeveichle/" + vTypeRep.findAll().get(0).getId())
    			.content(objectMapper.writeValueAsString(args))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	
    	assertEquals(1, vTypeRep.count());
    	assertEquals("new name", vTypeRep.findAll().get(0).getName());
    }
	
}
