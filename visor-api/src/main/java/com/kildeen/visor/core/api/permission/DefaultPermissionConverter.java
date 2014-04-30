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

package com.kildeen.visor.core.api.permission;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * <p>File created: 2014-02-16 01:35</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
public class DefaultPermissionConverter implements PermissionConverter {
    private static final String PERMISSION = "{\"t\":0";
    private static final String MINIMIZED_PERMISSION = "{\"t\":1";

    @Inject
    private PermissionResolver permissionResolver;

    private static final Splitter splitter = Splitter.on("*");
    private static final Splitter dotSplitter = Splitter.on(".");
    private Class<Permission> permissionImplClass;

    @Override
    public String getId(Class<?> permissionClass) {
        String permission = permissionClass.getCanonicalName();
        int firstFolderIndex = permission.indexOf(".");
        firstFolderIndex++; // remove dot too.
        return permissionClass.getCanonicalName().substring(firstFolderIndex);
    }

    @Override
    public String getPartId(final Class<? extends PartPermission> permissionClass) {
        String permission = WordUtils.uncapitalize(permissionClass.getEnclosingClass()
                .getSimpleName()) + permissionClass.getSimpleName();
        return permission;
    }

    @Override
    public String serialize(final Permission permission) {
        Gson gson = new Gson();
        return gson.toJson(permission, permission.getClass());
    }

    @Override
    public Permission deserialize(final String deserialized) {
        Gson gson = new GsonBuilder().create();
            return (Permission) gson.fromJson(deserialized, permissionImplClass);
    }

    @Override
    public Collection<String> serializeAll(final Collection<Permission> permissions) {
        List<String> serializedPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            serializedPermissions.add(serialize(permission));
        }
        return serializedPermissions;
    }

    @Override
    public Set<Permission> deserializeAll(final Collection<String> deserialized) {
        if (CollectionUtils.isNotEmpty(deserialized)) {
            Set<Permission> deserializedPermissions = new ListOrderedSet<>();
            for (String permission : deserialized) {
                deserializedPermissions.add(deserialize(permission));
            }
            return deserializedPermissions;
        }
        else {
            return Collections.emptySet();
        }

    }

    @PostConstruct
    private void init() {

        try {
            this.permissionImplClass = (Class<Permission>) Class.forName("com.kildeen.visor.core.permission.PermissionImpl");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PermissionImpl missing", e);
        }


    }

}
