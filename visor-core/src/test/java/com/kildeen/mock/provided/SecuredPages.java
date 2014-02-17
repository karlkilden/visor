package com.kildeen.mock.provided;

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.Secured;

@Secured(PermissionAccessDecisionVoter.class)
public interface SecuredPages {}