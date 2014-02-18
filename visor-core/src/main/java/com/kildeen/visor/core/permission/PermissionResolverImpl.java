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

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import com.kildeen.visor.core.api.permission.*;
import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.security.api.authorization.Secured;
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
    private DuplicatePartPermissionValidator duplicatePartPermissionValidator = new DuplicatePartPermissionValidator();

    private static final Logger log = LoggerFactory.getLogger(PermissionResolverImpl.class);
    private List<Permission> permissions = new ArrayList<>();
    private List<PermissionFolder> permissionFolders = new ArrayList<>();

    // cleared again when mapping is completed
    private Map<Class<?>, Permission> securedPermissions = new HashMap<>();
    private Set<Class<?>> securedFolders = new HashSet<>();

    @PostConstruct
    public void init() {
        mapPermissions();
        mapPermissionFolders();
        securedPermissions = null;
        securedFolders = null;
    }

    private void mapPermissionFolders() {
        log.info("ViewConfigDescriptors will now be mapped to permissionFolders");
        //When we find folders in the hierarchy we have no way of knowing if they are
        // secured and we need the full picture beforehand
        findSecuredFolders();
        for (ConfigDescriptor<?> configDescriptor : viewConfigResolver.getConfigDescriptors()) {
            Class<?> clazz = configDescriptor.getConfigClass();
            PermissionFolder folder = new PermissionFolder(new ArrayList<PermissionModel>(), "root", "");
            mapStructure(configDescriptor, clazz, folder);
        }
    }

    private void mapStructure(ConfigDescriptor<?> configDescriptor, Class<?> clazz, PermissionFolder folder) {
        if (isFolder(clazz) && folderIsSecured(configDescriptor)) {
            List<PermissionModel> folderPermissions = new ArrayList<>();
            for (Class<?> child : clazz.getDeclaredClasses()) {
                if (securedPermissions.containsKey(child)) {
                    folderPermissions.add(securedPermissions.get(child));
                } else if (securedFolders.contains(child)) {
                    mapStructure(configDescriptor, clazz, folder);
                }

            }
            PermissionFolder permissionFolder = new PermissionFolder(folderPermissions,
                    permissionConverter.getPermissionFolder(clazz), configDescriptor.getPath());
            permissionFolders.add(permissionFolder);
            folder.getAllChildren().add(permissionFolder);

        }
    }

    private boolean folderIsSecured(ConfigDescriptor<?> configDescriptor) {
        CallbackDescriptor callback = configDescriptor.getCallbackDescriptor(Secured.class);
        return checkIfSecured(callback);
    }

    private void findSecuredFolders() {
        for (ConfigDescriptor<?> descriptor : viewConfigResolver.getConfigDescriptors()) {
            if (isFolder(descriptor.getConfigClass()) && folderIsSecured(descriptor)) {
                securedFolders.add(descriptor.getConfigClass());
            }
        }
    }

    private boolean isFolder(Class<?> configClass) {
        return configClass.isInterface();
    }

    private boolean isView(Class<?> child) {
        return child.isInterface() == false;
    }

    private void mapPermissions() {
        log.info("ViewConfigDescriptors will now be mapped to Permissions");
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigResolver.getViewConfigDescriptors()) {
            boolean isSecured = folderIsSecured(viewConfigDescriptor);
            if (isSecured) {
                String stringPermission = permissionConverter.getPermission(viewConfigDescriptor.getConfigClass());
                Permission permission = new Permission(stringPermission, viewConfigDescriptor.getViewId());
                addChildren(viewConfigDescriptor, permission);
                securedPermissions.put(viewConfigDescriptor.getConfigClass(), permission);
                permissions.add(permission);
            }
        }
    }

    private boolean checkIfSecured(CallbackDescriptor callback) {
        if (callback != null && callback.getCallbackMethods() != null) {
            if (callback.getCallbackMethods().containsKey(PermissionAccessDecisionVoter.class)) {
                return true;
            }
        }
        return false;
    }

    private void addChildren(final ViewConfigDescriptor viewConfigDescriptor, final Permission permission) {
        for (Class<?> partPermission : viewConfigDescriptor.getConfigClass().getDeclaredClasses()) {
            if (partPermission.isAssignableFrom(PartPermission.class)) {
                String p = getValidatedPartPermission((Class<? extends PartPermission>) partPermission);
                Permission child = new Permission(p, viewConfigDescriptor.getViewId());
                permission.getChildren().add(child);
            }
        }
    }

    private String getValidatedPartPermission(final Class<? extends PartPermission> partPermission) {
        String p = permissionConverter.getPartPermission(partPermission);
        try {
            duplicatePartPermissionValidator.add(p);
        } catch (Exception e) {
            log.error("Duplicate PartPermission found {}", partPermission.getName());
            throw new RuntimeException("Duplicate PartPermission found " + partPermission.getName());
        }
        return p;
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
    public List<PermissionFolder> getPermissionFolders() {
        return permissionFolders;
    }

    @Override
    public void boot() {
        log.debug("Eager booting was triggered");
    }


}
