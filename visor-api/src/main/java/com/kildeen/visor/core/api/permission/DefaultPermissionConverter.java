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

package com.kildeen.visor.core.api.permission;

import com.google.gson.Gson;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <p>File created: 2014-02-16 01:35</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class DefaultPermissionConverter implements PermissionConverter {


    @Override
    public String getPermission(Class<?> permissionClass) {
        String permission = permissionClass.getCanonicalName();
        int firstFolderIndex = permission.indexOf(".");
        firstFolderIndex++; // remove dot too.
        return permissionClass.getCanonicalName().substring(firstFolderIndex);
    }

    @Override
    public String getPartPermission(final Class<? extends PartPermission> permissionClass) {
        String permission = WordUtils.uncapitalize(permissionClass.getEnclosingClass()
                .getSimpleName())+permissionClass.getSimpleName();
        return permission;
    }

    /*
     * Here for symmetry only
     */
    @Override
    public String getPermissionFolder(final Class<?> permissionFolderClass) {
       return getPermission(permissionFolderClass);
    }

    @Override
    public String serialize(final Permission permission) {
        Gson gson = new Gson();
        return gson.toJson(permission, Permission.class);
    }

    @Override
    public Permission deserialize(final String deserializedPermission) {
        Gson gson = new Gson();
        return gson.fromJson(deserializedPermission, Permission.class);

    }

    @Override
    public Collection<String> serializeAll(final Collection<Permission> permissions) {
        List<String> serializedPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
/*
            serializedPermissions.add(serialize(permission));
*/
        }
        return serializedPermissions;
    }

    @Override
    public Collection<Permission> deserializeAll(final Collection<String> serializedPermissions) {
        List<Permission> deserializedPermissions = new ArrayList<>();
        for (String deserializedPermission : serializedPermissions) {
            deserializedPermissions.add(deserialize(deserializedPermission));
        }
        return deserializedPermissions;
    }
}
