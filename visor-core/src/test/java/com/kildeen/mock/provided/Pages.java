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

package com.kildeen.mock.provided;

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import com.kildeen.visor.core.api.context.PermissionDataHolder;
import com.kildeen.visor.core.api.permission.PartPermission;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.security.api.authorization.Secured;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
@Secured(value= PermissionAccessDecisionVoter.class)
public interface Pages extends ViewConfig, SecuredPages {

    public class Secured implements Pages {
        public class Part1 implements PartPermission  {}
    }
    public class NotSecure implements ViewConfig {}

    public interface NestedSecured extends Pages {
        public class NestedSecuredChild implements NestedSecured {}
    }
}
