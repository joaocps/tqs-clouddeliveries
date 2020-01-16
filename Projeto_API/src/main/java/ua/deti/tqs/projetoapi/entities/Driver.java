/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author lbarros
 */

@Entity
public class Driver implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private int contact;
    
    private boolean inService;
    
    @JsonIgnore
    @OneToOne(mappedBy = "driver")
    private Veichle veichle;

    public Driver(int contact) {
        this.contact = contact;
        this.inService = false;
    }
    
    public Driver(){
        this.inService = false;
    }

    public int getId() {
        return id;
    }

    public int getContact() {
        return contact;
    }

    public boolean isInService() {
        return inService;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    @Override
    public String toString() {
        return "Driver{" + "id=" + id + ", contact=" + contact + ", inService=" + inService + '}';
    }
    
    
    
}
