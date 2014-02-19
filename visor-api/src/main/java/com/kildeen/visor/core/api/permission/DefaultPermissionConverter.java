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

import com.google.gson.*;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>File created: 2014-02-16 01:35</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class DefaultPermissionConverter implements PermissionConverter {


    @Override
    public String getPermissionId(Class<?> permissionClass) {
        String permission = permissionClass.getCanonicalName();
        int firstFolderIndex = permission.indexOf(".");
        firstFolderIndex++; // remove dot too.
        return permissionClass.getCanonicalName().substring(firstFolderIndex);
    }

    @Override
    public String getPartPermissionId(final Class<? extends PartPermission> permissionClass) {
        String permission = WordUtils.uncapitalize(permissionClass.getEnclosingClass()
                .getSimpleName()) + permissionClass.getSimpleName();
        return permission;
    }

    /*
     * Here for symmetry only
     */
    @Override
    public String getPermissionGroupId(final Class<?> permissionFolderClass) {
        return getPermissionId(permissionFolderClass);
    }

    @Override
    public String serialize(final PermissionModel permission) {
        Gson gson = new Gson();
        return gson.toJson(permission, permission.getClass());
    }

    @Override
    public Permission deserialize(final String deserialized) {
        Gson gson = new GsonBuilder().registerTypeAdapter(PermissionModel.class, new ObjectDeserializer()).create();
        return gson.fromJson(deserialized, Permission.class);

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
    public Collection<Permission> deserializeAll(final Collection<String> deserialized) {
        List<Permission> deserializedPermissions = new ArrayList<>();
        for (String permission : deserialized) {
            deserializedPermissions.add(deserialize(permission));
        }
        return deserializedPermissions;
    }

    private class MoneyInstanceCreator implements InstanceCreator<PermissionModel> {
        String id;

        public MoneyInstanceCreator(String deserialized) {
            id = deserialized;
        }

        public PermissionModel createInstance(Type type) {
            // t stands for type. 0 means Permission and PermissionGroup == 1.
            if (id.startsWith("{\"t\":0"))
                return new Permission();
            else if (id.startsWith("{\"t\":1"))
                return new PermissionGroup();
            else
                throw new RuntimeException("illegal type");
        }


    }
    public class ObjectDeserializer implements JsonDeserializer<PermissionModel> {

        @Override
        public PermissionModel deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            String value = element.getAs
            return null;
        }

    }
}
