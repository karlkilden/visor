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

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.text.WordUtils;

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
    @Inject
    private PermissionMinimizer minimizer;
    @Inject
    private PermissionRevisionWriter permissionRevisionWriter;

    private static final Splitter splitter = Splitter.on("*");
    private static final Splitter dotSplitter = Splitter.on(".");


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
        return gson.fromJson(deserialized, PermissionImpl.class);

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
        Set<Permission> deserializedPermissions = new ListOrderedSet<>();
        for (String permission : deserialized) {
            deserializedPermissions.add(deserialize(permission));
        }
        return deserializedPermissions;
    }

    @Override
    public List<Permission> expand(String minimizedPermissions) {
        Gson gson = new Gson();
        MinimizedPermission minimizedPermission = gson.fromJson(minimizedPermissions, MinimizedPermission.class);
        List<Permission> result = new ArrayList<>();
        for (String permission : minimizedPermission.getTruncatedPermissionModel()) {

            if (permission.contains("*")) {
                List<String> permissionData = splitter.splitToList(permission);
                PermissionImpl actual = (PermissionImpl) permissionResolver.getPermissionModel(permissionData.get(0));
                if (permissionData.get(1).isEmpty()) {
                    // do nothing
                } else {
                    String crud = permissionData.get(1);
                    actual.setCreate(crud.contains("C"));
                    actual.setRead(crud.contains("R"));
                    actual.setUpdate(crud.contains("U"));
                    actual.setDelete(crud.contains("D"));

                }
                result.add(actual);
            } else {
                Permission m = permissionResolver.getMaximized(permission);
                result.add(m);
            }
        }

        return result;
    }

    @Override
    public String minimize(Collection<Permission> minimizedPermissions) {
        MinimizedPermission result = minimizer.minimize(minimizedPermissions);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @Override
    public Set<Permission> automaticDeserializeAll(Collection<String> permissions) {
        Set<Permission> deserializedPermissions = new ListOrderedSet<>();
        for (String data : permissions) {
            if (data.startsWith(PERMISSION)) {
                deserializedPermissions.add(deserialize(data));
            } else if (data.startsWith(MINIMIZED_PERMISSION)) {
                deserializedPermissions.addAll(expand(data));
            }
        }
        return deserializedPermissions;
    }

    @Override
    public String automaticSerializeAll(Collection<Permission> permissions) {
        if (permissionRevisionWriter.isActivated()) {
            List<Permission> shortHand = new ArrayList<>();
            Multimap<String, Permission> hierarchy = ArrayListMultimap.create();
            for (Permission p : permissions) {
                String part = "";
                for (String s : dotSplitter.split(p.getId())) {
                    if (part.isEmpty()) {
                        part = s;
                    } else {
                        part = part + "." + s;
                    }
                    hierarchy.put(part, p);
                }
            }
            for (Map.Entry<String, Collection<Permission>> set : hierarchy.asMap().entrySet()) {
                Permission p = permissionResolver.getPermissionModel(set.getKey());
                if (shortHand != null) {
                    if (p.getCount() == permissions.size()) {
                        shortHand.add(p);
                        return minimize(shortHand);
                    }
                }
            }

            for (Map.Entry<String, Collection<Permission>> set : hierarchy.asMap().entrySet()) {
                Permission p = permissionResolver.getPermissionModel(set.getKey());
                if (shortHand != null) {
                    if (p.getCount() < permissions.size()) {
                        shortHand.add(p);
                        return minimize(shortHand);
                    }
                }
            }
        }
        return null;
    }

}
