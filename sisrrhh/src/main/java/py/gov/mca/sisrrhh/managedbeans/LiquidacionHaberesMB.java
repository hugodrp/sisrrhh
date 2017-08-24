/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.managedbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author vinsfran
 */
@Named(value = "liquidacionHaberesMB")
@SessionScoped
public class LiquidacionHaberesMB implements Serializable{
    
    
    public LiquidacionHaberesMB() {
    }
    
     @PostConstruct
    public void init() {
    }
    
}
