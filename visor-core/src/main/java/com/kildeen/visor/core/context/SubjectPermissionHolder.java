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

import com.google.common.collect.ImmutableMap;
import com.kildeen.visor.core.api.context.PermissionHolder;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.permission.PermissionImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>File created: 2014-02-16 17:14</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@SessionScoped
class SubjectPermissionHolder implements Serializable {

    @Inject
    private PermissionHolder permissionHolder;

    private Map<String, Permission> mappedPermissions;
    public static final Permission NOT_FOUND = new PermissionImpl(null, null, null);

    @PostConstruct
    private void init() {
        ImmutableMap.Builder<String, Permission> builder = ImmutableMap.<String, Permission>builder();
        for (Permission permission : permissionHolder.getPermissions()) {
            map(builder, permission);
        }
        mappedPermissions = builder.build();

    }

    private void map(final ImmutableMap.Builder<String, Permission> builder, Permission permission) {
        builder.put(permission.getId(), permission);
        for (Permission child : permission.getChildren()) {
            map(builder, child);
        }
    }

    public boolean hasPermission(String permission) {
        return mappedPermissions.containsKey(permission);
    }

    public Permission getPermission(String permission) {
        Permission p = mappedPermissions.get(permission);
        if (p == null) {
            return NOT_FOUND;
        }
        else {
            return p;
        }
    }
}
