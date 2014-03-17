package com.kildeen.visor.ui.configuration;

import com.kildeen.visor.core.api.context.VisorConfig;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-03-13
 */
public class TreeConfigurationResolver {
      private boolean sendForSelect;
    private boolean sendForCheck;
    private boolean sendForUncheck;
    private String onSelectRender;
    private String onCheckRender;


    public void init () {
        String dbUserName = ConfigResolver.getPropertyValue("databaseconfig.username");
    }
}
