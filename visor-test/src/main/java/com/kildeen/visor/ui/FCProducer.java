package com.kildeen.visor.ui;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
@ApplicationScoped
public class FCProducer implements Serializable {

    @Produces
    @RequestScoped
    private FacesContext getFc () {
        return FacesContext.getCurrentInstance();
    }
}
