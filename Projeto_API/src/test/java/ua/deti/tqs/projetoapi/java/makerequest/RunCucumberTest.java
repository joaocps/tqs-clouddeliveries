/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.makerequest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;



/**
 *
 * @author lbarros
 */

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/main/resources/features/makerequest")
public class RunCucumberTest {
    
}