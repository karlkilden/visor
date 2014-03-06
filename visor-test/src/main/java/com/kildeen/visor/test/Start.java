package com.kildeen.visor.test;

import org.omnifaces.config.WebXml;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-03-06
 */
@WebListener
public class Start implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebXml.INSTANCE.init(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
