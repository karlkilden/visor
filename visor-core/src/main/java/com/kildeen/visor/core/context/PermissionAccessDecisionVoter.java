/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.kildeen.visor.core.context;

import com.kildeen.visor.core.api.context.PermissionContext;
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
public class PermissionAccessDecisionVoter implements AccessDecisionVoter {

    @Inject
    private PermissionContext permissionContext;
    public Set<SecurityViolation> checkPermission(AccessDecisionVoterContext accessDecisionVoterContext) {

        if(permissionContext.isAllowed()) {
            return null;
        }
        else {
            Set<SecurityViolation> violations = new HashSet<>();
            violations.add(new SecurityViolationImpl("Failed Visor Security check"));
            return violations;
        }
    }
}
