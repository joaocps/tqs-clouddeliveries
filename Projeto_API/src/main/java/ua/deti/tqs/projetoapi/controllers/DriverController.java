package ua.deti.tqs.projetoapi.controllers;

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

import ua.deti.tqs.projetoapi.entities.Driver;
import ua.deti.tqs.projetoapi.repositories.DriverRep;

@RestController
@RequestMapping("/driver")
public class DriverController {
	
	private static final  String ERROR = "ERROR! :";
    private static final  Logger LOGGER = Logger.getLogger(DriverController.class);


    @Autowired
    DriverRep driverRep;

    @GetMapping("/delete")
    public @ResponseBody
    String delete() {
        driverRep.deleteAll();
        return "success";
    }

    @GetMapping("/")
    public @ResponseBody
    Iterable<Driver> getAllDrivers() {
        return driverRep.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Optional<Driver> getDriver(@PathVariable("id") int id) {
        return driverRep.findById(id);
    }

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity postDriver(@RequestBody Map<String, Object> arg) {
        try {
            int contact = Integer.parseInt(arg.get("contact").toString());
            Driver driver = new Driver(contact);
            driverRep.save(driver);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
        	LOGGER.error(ERROR + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteDriver(@PathVariable("id") int id) {
        driverRep.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity putDriver(@PathVariable("id") int id, @RequestBody Map<String, Object> arg) {
        try {
            int contact = Integer.parseInt(arg.get("contact").toString());
            Optional<Driver> aux = driverRep.findById(id);
            Driver driver;
            if (aux.isPresent()) {
                driver = aux.get();
                driver.setContact(contact);
                driverRep.save(driver);
                return new ResponseEntity(HttpStatus.OK);

            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
        	LOGGER.error(ERROR + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
