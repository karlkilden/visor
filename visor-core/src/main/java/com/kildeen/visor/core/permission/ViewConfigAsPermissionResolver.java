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

import com.kildeen.visor.core.api.permission.PartPermission;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.security.api.authorization.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>File created: 2014-02-16 21:13</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
public class ViewConfigAsPermissionResolver implements Serializable {

    @Inject
    private ViewConfigResolver viewConfigResolver;
    @Inject
    private PermissionConverter permissionConverter;
    private DuplicatePartPermissionValidator duplicatePartPermissionValidator = new DuplicatePartPermissionValidator();

    private static final Logger log = LoggerFactory.getLogger(ViewConfigAsPermissionResolver.class);
    private List<Permission> permissions = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigResolver.getViewConfigDescriptors()) {
            if (viewConfigDescriptor.getCallbackDescriptor(Secured.class) != null) {
                Permission permission = new Permission();
                permission.setPermission(permissionConverter.getPermission(viewConfigDescriptor.getConfigClass()));
                addChildren(viewConfigDescriptor, permission);
                permissions.add(permission);
            }
        }
    }

    private void addChildren(final ViewConfigDescriptor viewConfigDescriptor, final Permission permission) {
        for (Class<?> partPermission : viewConfigDescriptor.getConfigClass().getDeclaredClasses()) {
            if (partPermission.isAssignableFrom(PartPermission.class)) {
                Permission child = new Permission();
                String p = getValidatedPartPermission(partPermission, child);
                child.setPermission(p);
                permission.getChildren().add(child);
            }
        }
    }

    private String getValidatedPartPermission(final Class<?> partPermission, final Permission child) {
        String p = permissionConverter.getPartPermission(partPermission);
        try {
            duplicatePartPermissionValidator.add(p);
        } catch (Exception e) {
            log.error("Duplicate PartPermission found {}", child.getClass().getName());
            throw new RuntimeException("Duplicate PartPermission found " + child.getClass().getName());
        }
        return p;
    }



}