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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private List<Permission> permissions = new ArrayList<>();
    private PermissionMappingContext mappingContext;
    private List<PermissionModel> rootPermissionModels = new ArrayList<>();

    @PostConstruct
    public void init() {
        mappingContext = new PermissionMappingContext(viewConfigResolver.getConfigDescriptors());
        createStructure();
        mappingContext = null;
    }

    @Override
    public List<Permission> getPermissions() {
        List<Permission> clonedPermissions = new ArrayList<>(this.permissions.size());
        for (Permission permission : this.permissions) {
            Permission clone = new Permission(permission);
            clonedPermissions.add(new Permission(permission));
        }
        return clonedPermissions;
    }

    @Override
    public List<PermissionModel> getRootNodes() {
        return rootPermissionModels;
    }

    private void createStructure() {
        log.info("ViewConfigDescriptors will now be mapped");
        for (ConfigDescriptor<?> configDescriptor : viewConfigResolver.getConfigDescriptors()) {
            if (mappingContext.shouldMap(configDescriptor)) {
               rootPermissionModels.add(mapToStructure(configDescriptor, null, null));
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

                        children.add(mapToStructure(descriptor, child, permissionGroupParent));

                    } else {
                        ConfigDescriptor descriptor = mappingContext.getMappedPermissionModels().get(child);
                        children.add(mapPermissionToStructure(descriptor));
                    }
                }
            }
        } else {
            ConfigDescriptor viewDescriptor = mappingContext.getMappedPermissionModels().get(clazz);
            PermissionModel rootPermission = mapPermissionToStructure(viewDescriptor);
            return rootPermission;
        }
        PermissionGroup newGroup = new PermissionGroup(permissionConverter.getPermissionFolder(clazz), children, configDescriptor);
        if (permissionGroupParent != null) {
            permissionGroupParent.getChildren().add(newGroup);
            return permissionGroupParent;
        }
        return newGroup;
    }

    private PermissionModel mapPermissionToStructure(ConfigDescriptor configDescriptor) {
        String id = permissionConverter.getPermission(configDescriptor.getConfigClass());
        Set<PermissionModel> children = getChildren(configDescriptor);
        Permission permission = new Permission(id, children, configDescriptor);
        permissions.add(permission);
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
        String p = permissionConverter.getPartPermission(partPermission);
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
