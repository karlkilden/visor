package com.kildeen.visor.test;

import com.kildeen.visor.core.api.context.DefaultAccessDecisionVoter;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.security.api.authorization.Secured;

@Secured(DefaultAccessDecisionVoter.class)
public interface SecuredPages {}