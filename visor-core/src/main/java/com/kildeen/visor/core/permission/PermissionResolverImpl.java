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
    public List<PermissionModel> getPermissions() {
        List<PermissionModel> clonedPermissions = new ArrayList<>(state.getPermissions().size());
        for (Permission permission : state.getPermissions()) {
            Permission clone = new Permission(permission);
            clonedPermissions.add(new Permission(permission));
        }
        return clonedPermissions;
    }

    @Override
    public List<PermissionModel> getPermissionModels() {
        return state.getPermissionModels();
    }

    @Override
    public List<PermissionModel> getRootPermissionModels() {
        return state.getRootPermissionModels();
    }

    @Override
    public PermissionModel getPermissionModel(String id) {
        return state.getMappedPermissionModels().get(id);
    }

    @Override
    public PermissionModel getMaximized(String id) {
        PermissionModel model = state.getMappedPermissionModels().get(id);
        if (model instanceof Permission) {
            Permission p = (Permission) model;
              p.privilege();

        }
    }

    private void createStructure() {
        log.info("ViewConfigDescriptors will now be mapped");
        for (ConfigDescriptor<?> configDescriptor : viewConfigResolver.getConfigDescriptors()) {
            if (mappingContext.isSecuredRoot(configDescriptor)) {
                state.add(mapToStructure(configDescriptor, null, null), true);
            }
        }
    }

    /*
     * This method will create the structure recursively. Whenever it finds another subfolder that will get created
     * first. Only when a node contains nothing but Permissions will a group get created.
     */
    private PermissionModel mapToStructure(ConfigDescriptor<?> configDescriptor, Class<?> childClass, PermissionGroup permissionGroupParent) {
        Class<?> clazz = childClass;
        if (clazz == null) {
            //This is a root call and not a recursive call thus no child
            clazz = configDescriptor.getConfigClass();
        }
        Set<PermissionModel> children = new ListOrderedSet<>();
        if (mappingContext.isFolder(clazz)) {
            for (Class<?> child : clazz.getDeclaredClasses()) {
                if (mappingContext.getMappedPermissionModels().containsKey(child)) {
                    if (mappingContext.isFolder(child)) {
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissionModels().get(child);
                        PermissionModel model =  mapToStructure(descriptor, child, permissionGroupParent);
                        children.add(model);
                        state.add(model, false);

                    } else {
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissionModels().get(child);
                        PermissionModel model =  mapPermissionToStructure(descriptor, false);

                        children.add(model);
                        state.add(model, false);
                    }
                }
            }
        } else {
            ConfigDescriptor viewDescriptor = mappingContext.getMappedPermissionModels().get(clazz);
            PermissionModel rootPermission = mapPermissionToStructure(viewDescriptor, true);
            return rootPermission;
        }
        PermissionGroup newGroup = new PermissionGroup(permissionConverter.getPermissionGroupId(clazz), children, configDescriptor);
        if (permissionGroupParent != null) {
            permissionGroupParent.getChildren().add(newGroup);
            return permissionGroupParent;
        }
        return newGroup;
    }

    private PermissionModel mapPermissionToStructure(ConfigDescriptor configDescriptor, boolean isRoot) {
        String id = permissionConverter.getPermissionId(configDescriptor.getConfigClass());
        Set<PermissionModel> children = getChildren(configDescriptor);
        Permission permission = new Permission(id, children, configDescriptor);
        state.add(permission, isRoot);
        return permission;
    }

    private Set<PermissionModel> getChildren(final ConfigDescriptor viewConfigDescriptor) {
        final Set<PermissionModel> children = new ListOrderedSet<>();
        for (Class<?> partPermission : viewConfigDescriptor.getConfigClass().getDeclaredClasses()) {
            if (partPermission.isAssignableFrom(PartPermission.class)) {
                String p = getValidatedPartPermission((Class<? extends PartPermission>) partPermission);
                Permission child = new Permission(p, null, viewConfigDescriptor);
                children.add(child);
            }
        }
        return children;
    }

    private String getValidatedPartPermission(final Class<? extends PartPermission> partPermission) {
        String p = permissionConverter.getPartPermissionId(partPermission);
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
