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
import com.kildeen.visor.core.api.permission.PermissionConverter;
import com.kildeen.visor.core.api.permission.PermissionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-21
 */
@ApplicationScoped
class PermissionManagerImpl implements PermissionManager {

    @Inject
    private PermissionConverter permissionConverter;

    @Override
    public Collection<String> toStrings(Collection<Permission> permissions) {
        return permissionConverter.serializeAll(permissions);
    }

    @Override
    public Set<Permission> toPermissions(Collection<String> permissions) {
        return permissionConverter.deserializeAll(permissions);
    }

    @Override
    public Collection<String> toStrings(Permission... permissions) {
        List<String> result = new ArrayList<>();

        for (Permission permission : permissions) {
            result.add(permissionConverter.serialize(permission));
        }
        return result;
    }

    @Override
    public Set<Permission> toPermissions(String... permissions) {
        Set<Permission> result = new HashSet<>();

        for (String permission : permissions) {
            result.add(permissionConverter.deserialize(permission));
        }
        return result;
    }
}
