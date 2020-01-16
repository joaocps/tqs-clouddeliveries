/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author lbarros
 */
@Entity
public class Veichle implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private VeichleType type;
    
    private boolean used;
    
    @OneToMany(mappedBy="veichle")
    private List<Order> orders;
    
    

    
    public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    public Veichle(VeichleType type) {
        this.type = type;
        this.used = false;
    }
    
    public Veichle(VeichleType vType, Driver driver){
    	this.type = vType;
    	this.driver = driver;
    }
    
    

    public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Veichle() {
        this.used = false;
    }

    public int getId() {
        return id;
    }

    public VeichleType getType() {
        return type;
    }

    public boolean isUsed() {
        return used;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(VeichleType type) {
        this.type = type;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "Veichle{" + "id=" + id + ", type=" + type + ", used=" + used + '}';
    }

}
