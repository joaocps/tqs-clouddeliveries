/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class VeichleType implements Serializable{
    
    

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String name;
    private double capacity;
    
    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<Veichle> veichles;

    

    public VeichleType(String name, double capacity) {
        this.name = name;
        this.capacity = capacity;
    }
    
    public VeichleType(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    
    public void setVeichles(List<Veichle> veichles) {
        this.veichles = veichles;
    }
    
    

    public List<Veichle> getVeichles() {
        return veichles;
    }

    @Override
    public String toString() {
        return "VeichleType{" + "id=" + id + ", name=" + name + ", capacity=" + capacity + '}';
    }
    
    
}
