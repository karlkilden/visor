package com.kildeen.mock.provided;

import com.kildeen.visor.core.api.context.DefaultAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.Secured;

@Secured(DefaultAccessDecisionVoter.class)
public interface SecuredPages {}