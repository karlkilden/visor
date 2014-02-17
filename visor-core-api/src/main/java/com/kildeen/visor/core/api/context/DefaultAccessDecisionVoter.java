package com.kildeen.visor.core.api.context;

import org.apache.deltaspike.security.api.authorization.AccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public class DefaultAccessDecisionVoter implements AccessDecisionVoter {

    @Inject
    private PermissionContext permissionContext;
    public Set<SecurityViolation> checkPermission(AccessDecisionVoterContext accessDecisionVoterContext) {

        if(permissionContext.isAllowed()) {
            return null;
        }
        else {
            Set<SecurityViolation> violations = new HashSet<>();
            violations.add(new MockSecurityViolation("failed"));
            return violations;
        }
    }
}
