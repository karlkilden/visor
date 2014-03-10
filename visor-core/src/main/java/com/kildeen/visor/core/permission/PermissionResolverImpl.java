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
    public static final PermissionModel NOT_FOUND = new PermissionModelImpl("NOT_FOUND", null, null);

    @Inject
    private ViewConfigResolver viewConfigResolver;
    @Inject
    private PermissionConverter permissionConverter;

    private static final Logger log = LoggerFactory.getLogger(PermissionResolverImpl.class);
    private PermissionMappingContext mappingContext;
    private PermissionResolverState state;


    @PostConstruct
    public void init() {
        // get context so we know if and how each ViewConfig should be mapped.
        mappingContext = new PermissionMappingContext(viewConfigResolver.getConfigDescriptors());
        // All Permission state is kept in a state object with read only mode.
        state = new PermissionResolverState();
        //We now our ViewConfigs and the state object is ready to be populated.
        createStructure();
        // mappingContext is only needed during mapping.
        mappingContext = null;
    }


    @Override
    public List<Permission> getPermissions() {
        List<Permission> clonedPermissions = new ArrayList<>(state.getPermissions().size());
        for (PermissionModel permission : state.getPermissions()) {
            PermissionImpl clone = new PermissionImpl(permission);
            clonedPermissions.add(clone);
        }
        return clonedPermissions;
    }

    @Override
    public List<Permission> getRootPermissions() {
        List<Permission> rootPermissions = new ArrayList<>(state.getRootPermissions().size());
        for (PermissionModel permission : state.getRootPermissions()) {
            PermissionImpl clone = new PermissionImpl(permission);
            rootPermissions.add(clone);
        }
        return rootPermissions;
    }

    @Override
    public Permission getPermission(String id) {
        PermissionModel p =  state.getMappedPermissions().get(id);
        if (p == null) {
            p = NOT_FOUND;
        }
        return new PermissionImpl(p);
    }

    @Override
    public Permission getPermission(final Class<?> clazz) {
        return getPermission(permissionConverter.getId(clazz));
    }

    @Override
    public Permission getMaximized(String id) {
        Permission model = getPermission(id);
        model.privilege();
        return model;
    }

    private void createStructure() {
        log.info("ViewConfigDescriptors will now be mapped");
        for (ConfigDescriptor<?> configDescriptor : viewConfigResolver.getConfigDescriptors()) {
            // We only go ahead with the mapping if it's a root level ViewConfig.
            // Otherwise a child would be mapped first as a single and then later when discovered as child
            if (mappingContext.isSecuredRoot(configDescriptor)) {
                PermissionModel permission = createPermission(configDescriptor, null, null);
                state.add(permission, true);
            }
        }
    }

    /*
     * This method will create a Permission and works recursively to map all children. Whenever it finds another subfolder that will get created
     * first. Only when a node contains nothing but Permissions will a group get created.
     */
    private PermissionModel createPermission(ConfigDescriptor<?> configDescriptor, Class<?> childClass, PermissionModel permissionParent) {
        Class<?> clazz = childClass;
        if (clazz == null) {
            //This is a root call and not a recursive call thus no child.
            clazz = configDescriptor.getConfigClass();
        }
        Set<PermissionModel> children = new ListOrderedSet<>();
        if (mappingContext.isFolder(clazz)) {
            // Folder found, children needs to be created first
            for (Class<?> child : clazz.getDeclaredClasses()) {
                // Only add children if they are secured
                if (mappingContext.getMappedPermissions().containsKey(child)) {
                    if (mappingContext.isFolder(child)) {
                        // This is the recursive part. The child is a folder thus needs to be created first. Important
                        // that it's ConfigDescriptor is used thus one strong reason for the mappingContext.
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissions().get(child);
                        PermissionModel permission =  createPermission(descriptor, child, permissionParent);
                        children.add(permission);
                        state.add(permission, false);

                    } else {
                        // Child is not a folder.
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissions().get(child);
                        PermissionModel model =  createPermissionWithPartPermissions(descriptor, false);

                        children.add(model);
                        state.add(model, false);
                    }
                }
            }
        } else {
             // Permission with no full worthy children. Might still have part permissions
            ConfigDescriptor viewDescriptor = mappingContext.getMappedPermissions().get(clazz);
            PermissionModel permission = createPermissionWithPartPermissions(viewDescriptor, true);
            return permission;
        }

        PermissionModel permission = new PermissionModelImpl(permissionConverter.getId(clazz), children, configDescriptor);
        // In a recursive call there's a parent
        if (permissionParent != null) {
            permissionParent.getChildren().add(permission);
            return permissionParent;
        }
        return permission;
    }

    private PermissionModel createPermissionWithPartPermissions(ConfigDescriptor configDescriptor, boolean isRoot) {
        String id = permissionConverter.getId(configDescriptor.getConfigClass());
        Set<PermissionModel> children = getChildren(configDescriptor);
        PermissionModel permission = new PermissionModelImpl(id, children, configDescriptor);
        state.add(permission, isRoot);
        return permission;
    }

    private Set<PermissionModel> getChildren(final ConfigDescriptor viewConfigDescriptor) {
        final Set<PermissionModel> children = new ListOrderedSet<>();
        for (Class<?> partPermission : viewConfigDescriptor.getConfigClass().getDeclaredClasses()) {
            if (partPermission.isAssignableFrom(PartPermission.class)) {
                String p = getValidatedPartPermission((Class<? extends PartPermission>) partPermission);
                PermissionModel child = new PermissionModelImpl(p, null, viewConfigDescriptor);
                children.add(child);
            }
        }
        return children;
    }

    private String getValidatedPartPermission(final Class<? extends PartPermission> partPermission) {
        // PartPermissions does not include package names and collisions are probable.
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

    @Override
    public PermissionModel getPermissionModel(final String currentNodeId) {
        return state.getMappedPermissions().get(currentNodeId);
    }
}
