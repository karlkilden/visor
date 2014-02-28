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

import com.kildeen.visor.core.api.permission.*;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * <p>File created: 2014-02-16 21:13</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
public class PermissionResolverImpl implements PermissionResolver {
    public static final Permission NOT_FOUND = new PermissionImpl("NOT_FOUND", null, null);

    @Inject
    private ViewConfigResolver viewConfigResolver;
    @Inject
    private PermissionConverter permissionConverter;

    private static final Logger log = LoggerFactory.getLogger(PermissionResolverImpl.class);
    private PermissionMappingContext mappingContext;
    private PermissionResolverState state;


    @PostConstruct
    public void init() {
        mappingContext = new PermissionMappingContext(viewConfigResolver.getConfigDescriptors());
        state = new PermissionResolverState(mappingContext);
        createStructure();
        mappingContext = null;
    }

    @Override
    public List<Permission> getPermissions() {
        List<Permission> clonedPermissions = new ArrayList<>(state.getPermissions().size());
        for (PermissionImpl permission : state.getPermissions()) {
            PermissionImpl clone = new PermissionImpl(permission);
            clonedPermissions.add(new PermissionImpl(permission));
        }
        return clonedPermissions;
    }

    @Override
    public List<Permission> getRootPermissions() {
        return state.getRootPermissions();
    }

    @Override
    public Permission getPermission(String id) {
        Permission p =  state.getMappedPermissions().get(id);
        if (p == null) {
            p = NOT_FOUND;
        }
        return p;
    }

    @Override
    public Permission getMaximized(String id) {
        Permission model = state.getMappedPermissions().get(id);
        model.privilege();
        return model;
    }

    private void createStructure() {
        log.info("ViewConfigDescriptors will now be mapped");
        for (ConfigDescriptor<?> configDescriptor : viewConfigResolver.getConfigDescriptors()) {
            if (mappingContext.isSecuredRoot(configDescriptor)) {
                state.add((PermissionImpl)mapToStructure(configDescriptor, null, null), true);
            }
        }
    }

    /*
     * This method will create the structure recursively. Whenever it finds another subfolder that will get created
     * first. Only when a node contains nothing but Permissions will a group get created.
     */
    private PermissionImpl mapToStructure(ConfigDescriptor<?> configDescriptor, Class<?> childClass, PermissionImpl permissionParent) {
        Class<?> clazz = childClass;
        if (clazz == null) {
            //This is a root call and not a recursive call thus no child
            clazz = configDescriptor.getConfigClass();
        }
        Set<Permission> children = new ListOrderedSet<>();
        if (mappingContext.isFolder(clazz)) {
            for (Class<?> child : clazz.getDeclaredClasses()) {
                if (mappingContext.getMappedPermissionModels().containsKey(child)) {
                    if (mappingContext.isFolder(child)) {
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissionModels().get(child);
                        PermissionImpl model =  mapToStructure(descriptor, child, permissionParent);
                        children.add(model);
                        state.add(model, false);

                    } else {
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissionModels().get(child);
                        PermissionImpl model =  mapPermissionToStructure(descriptor, false);

                        children.add(model);
                        state.add(model, false);
                    }
                }
            }
        } else {
            ConfigDescriptor viewDescriptor = mappingContext.getMappedPermissionModels().get(clazz);
            PermissionImpl rootPermission = mapPermissionToStructure(viewDescriptor, true);
            return rootPermission;
        }
        PermissionImpl newGroup = new PermissionImpl(permissionConverter.getId(clazz), children, configDescriptor);
        if (permissionParent != null) {
            permissionParent.getChildren().add(newGroup);
            return permissionParent;
        }
        return newGroup;
    }

    private PermissionImpl mapPermissionToStructure(ConfigDescriptor configDescriptor, boolean isRoot) {
        String id = permissionConverter.getId(configDescriptor.getConfigClass());
        Set<Permission> children = getChildren(configDescriptor);
        PermissionImpl permission = new PermissionImpl(id, children, configDescriptor);
        state.add(permission, isRoot);
        return permission;
    }

    private Set<Permission> getChildren(final ConfigDescriptor viewConfigDescriptor) {
        final Set<Permission> children = new ListOrderedSet<>();
        for (Class<?> partPermission : viewConfigDescriptor.getConfigClass().getDeclaredClasses()) {
            if (partPermission.isAssignableFrom(PartPermission.class)) {
                String p = getValidatedPartPermission((Class<? extends PartPermission>) partPermission);
                PermissionImpl child = new PermissionImpl(p, null, viewConfigDescriptor);
                children.add(child);
            }
        }
        return children;
    }

    private String getValidatedPartPermission(final Class<? extends PartPermission> partPermission) {
        String p = permissionConverter.getPartId(partPermission);
        try {
            mappingContext.addPermissionIdToUniqueCheckList(p);
        } catch (Exception e) {
            log.error("Duplicate PartPermission found {}", partPermission.getName());
            throw new RuntimeException("Duplicate PartPermission found " + partPermission.getName());
        }
        return p;
    }


    @Override
    public void boot() {
        log.debug("Eager booting was triggered");
    }
}
