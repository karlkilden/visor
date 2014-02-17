package com.kildeen.visor.test;

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.security.api.authorization.Secured;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
@Secured(PermissionAccessDecisionVoter.class)
public interface Pages extends ViewConfig, SecuredPages {
    public class Secured {}
}
