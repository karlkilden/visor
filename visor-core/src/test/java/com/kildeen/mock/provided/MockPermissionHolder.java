/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *  * or more contributor license agreements. See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership. The ASF licenses this file
 *  * to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package com.kildeen.mock.provided;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.context.PermissionHolder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>File created: 2014-02-16 18:09</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
public class MockPermissionHolder implements PermissionHolder {
    private Set<Permission> permissions;

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(final Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @PostConstruct
    private void init() {
        Permission p = new Permission("","");
        permissions = new HashSet<>();
        permissions.add(p);
        Permission p2 = new Permission("testTrue", "");
        permissions.add(p2);
    }
}
