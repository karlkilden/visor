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

import com.kildeen.visor.core.api.context.DefaultPermissionHolder;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.permission.PermissionImpl;
import com.kildeen.visor.core.permission.PermissionModel;
import com.kildeen.visor.core.permission.PermissionModelImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import java.util.Collections;
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
@Specializes
public class MockPermissionHolder extends DefaultPermissionHolder {
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
        PermissionModel t = new PermissionModelImpl("", Collections.<PermissionModel>emptySet(),null);

        PermissionImpl p = new PermissionImpl(t);
        permissions = new HashSet<>();
        permissions.add(p);
        PermissionModel t2 = new PermissionModelImpl("testTrue", Collections.<PermissionModel>emptySet(),null);

        PermissionImpl p2 = new PermissionImpl(t2);
        permissions.add(p2);
    }
}
