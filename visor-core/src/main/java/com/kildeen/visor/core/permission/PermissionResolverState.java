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

package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.Permission;
import org.apache.commons.collections4.set.ListOrderedSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds all state for the permission resolver
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
final class PermissionResolverState {
    private ListOrderedSet<PermissionImpl> permissions = new ListOrderedSet<>();
    private ListOrderedSet<Permission> rootPermissions = new ListOrderedSet<>();
    private Map<String, Permission> mappedPermissions = new HashMap<>();

    private void addToRelevantCollections(PermissionImpl permission, boolean isRoot) {
        if (permission instanceof PermissionImpl) {
            permissions.add((PermissionImpl) permission);

        }
        if (isRoot) {
            rootPermissions.add(permission);
        }
        permissions.add(permission);
        mappedPermissions.put(permission.getId(), permission);

    }

    public List<PermissionImpl> getPermissions() {
        return permissions.asList();
    }

    public List<Permission> getRootPermissions() {
        return rootPermissions.asList();
    }

    public Map<String, Permission> getMappedPermissions() {
        return mappedPermissions;
    }

    public void add(PermissionImpl permission, boolean isRoot) {
        addToRelevantCollections(permission, isRoot);
    }
}
