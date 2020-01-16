package ua.deti.tqs.projetoapi.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ua.deti.tqs.projetoapi.entities.VeichleType;
import ua.deti.tqs.projetoapi.repositories.VeichleTypeRep;

@RestController
@RequestMapping("/typeveichle")
public class VeichleTypeController {
	
	private static final  String ERROR = "ERROR! :";
    private static final  Logger LOGGER = Logger.getLogger(VeichleTypeController.class);
	
	@Autowired
	VeichleTypeRep vTypeRep;
	
	@GetMapping("/delete")
	public @ResponseBody String delete(){
		vTypeRep.deleteAll();
		return "success";
	}
	
	@GetMapping("/")
	public ResponseEntity<List<VeichleType>> getAllDrivers(){
		return new ResponseEntity<>(vTypeRep.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<VeichleType> getVeichleType(@PathVariable("id") int id){
		Optional<VeichleType> aux = vTypeRep.findById(id);
		if (aux.isPresent()){
			VeichleType type = aux.get();
			return new ResponseEntity<>(type, HttpStatus.OK) ;
		}
			
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		
	}
	
	@PostMapping(value = "/", consumes = "application/json")
	public  ResponseEntity postVeichleType(@RequestBody Map<String, Object> arg){
		try {
			String name = arg.get("name").toString();
			double cap = Double.parseDouble(arg.get("capacity").toString());
			VeichleType type = new VeichleType(name, cap);
			vTypeRep.saveAndFlush(type);
			return new ResponseEntity(HttpStatus.OK);
		} catch(Exception e){
			LOGGER.error(ERROR + e.getMessage());
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteVeichleType(@PathVariable("id") int id){
		if (vTypeRep.findById(id).isPresent()){
			vTypeRep.deleteById(id);
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity putVeichleType(@PathVariable("id") int id, @RequestBody Map<String, Object> arg){
		try {
			String name = arg.get("name").toString();
			double cap = Double.parseDouble(arg.get("capacity").toString());
			Optional<VeichleType> aux = vTypeRep.findById(id);
			VeichleType vType;
			if (aux.isPresent()){
				vType = aux.get();
				vType.setName(name);
				vType.setCapacity(cap);
				vTypeRep.save(vType);
				return new ResponseEntity(HttpStatus.OK);

			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch(Exception e){
			LOGGER.error(ERROR + e.getMessage());
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
