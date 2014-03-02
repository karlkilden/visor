package com.kildeen.visor.test;

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.security.api.authorization.Secured;

@Secured(PermissionAccessDecisionVoter.class)
public interface SecuredPages {}