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
import org.springframework.web.bind.annotation.RestController;

import ua.deti.tqs.projetoapi.entities.Driver;
import ua.deti.tqs.projetoapi.entities.Veichle;
import ua.deti.tqs.projetoapi.entities.VeichleType;
import ua.deti.tqs.projetoapi.repositories.DriverRep;
import ua.deti.tqs.projetoapi.repositories.VeichleRep;
import ua.deti.tqs.projetoapi.repositories.VeichleTypeRep;

@RestController
@RequestMapping("/veichle")
public class VeichleController {
	
	private static final  String ERROR = "ERROR! :";
    private static final  Logger LOGGER = Logger.getLogger(VeichleController.class);
	
	@Autowired
	VeichleRep vRep;
	
	@Autowired
	VeichleTypeRep cTypeRep;
	
	@Autowired
	DriverRep dRep;
	
	@GetMapping("/delete")
	public ResponseEntity delete(){
		vRep.deleteAll();
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Veichle>> getAllDrivers(){
		return new ResponseEntity<>(vRep.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Veichle> getVeichleType(@PathVariable("id") int id){
		Optional<Veichle> aux = vRep.findById(id);
		if (aux.isPresent()){
			Veichle veichle = aux.get();
			return new ResponseEntity<>(veichle, HttpStatus.OK) ;
		}
			
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		
	}
	
	@PostMapping(value = "/", consumes = "application/json")
	public  ResponseEntity postVeichleType(@RequestBody Map<String, Object> arg){
		try {
			int idVeichleType = Integer.parseInt(arg.get("id_veichle_type").toString());
			int idDriver = Integer.parseInt(arg.get("id_driver").toString());
			Optional<VeichleType> typee = cTypeRep.findById(idVeichleType);
			Optional<Driver> driverr = dRep.findById(idDriver);
			if (typee.isPresent() && driverr.isPresent()){
				VeichleType type = typee.get();
				Driver driver = driverr.get();
				driver.setInService(true);
				Veichle v = new Veichle(type, driver);
				vRep.saveAndFlush(v);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);

		} catch(Exception e){
			LOGGER.error(ERROR + e.getMessage());
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteVeichleType(@PathVariable("id") int id){
		if (vRep.findById(id).isPresent()){
			vRep.deleteById(id);
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity putVeichleType(@PathVariable("id") int id, @RequestBody Map<String, Object> arg){
		try {
			Optional<Veichle> vAux = vRep.findById(id);
			int idVeichleType = Integer.parseInt(arg.get("id_veichle_type").toString());
			int idDriver = Integer.parseInt(arg.get("id_driver").toString());
			Optional<VeichleType> typee = cTypeRep.findById(idVeichleType);
			Optional<Driver> driverr = dRep.findById(idDriver);
			if (typee.isPresent() && driverr.isPresent() && vAux.isPresent()){
				Veichle v = vAux.get();
				VeichleType type = typee.get();
				Driver driver = driverr.get();
				v.setType(type);
				v.setDriver(driver);
				vRep.saveAndFlush(v);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch(Exception e){
			LOGGER.error(ERROR + e.getMessage());
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
