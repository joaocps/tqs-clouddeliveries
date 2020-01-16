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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author lbarros
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserType implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private double price;
    private int numOrders;
    
    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<User> users;
    
    public UserType(){}
    
    
    
    public List<User> getUsers() {
		return users;
	}



	public void setUsers(List<User> users) {
		this.users = users;
	}



	public UserType(int numOrders, double price){
        this.numOrders = numOrders;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getNumOrders() {
        return numOrders;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

	@Override
	public String toString() {
		return "UserType [id=" + id + ", price=" + price + ", num_orders=" + numOrders  + "]";
	}
    
    
    
    
    
    
}
