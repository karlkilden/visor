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

package com.kildeen.visor.core.context;

import com.kildeen.visor.core.api.context.PermissionContext;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;
import com.kildeen.visor.core.permission.SubjectPermissionMapper;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.security.api.authorization.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This is the central implementation for PermissionContext. It uses {@link javax.faces.bean.ViewScoped} and as
 * a consequence it's dependent on Deltaspike porting this to CDI. This is to ensure interoperability between
 * JSF 2.x and JSF 2.2.
 *
 *  {@inheritDoc}
 *
 * <p>File created: 2014-02-15 16:28</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ViewScoped
@Named("PermissionContext")
public class PermissionContextImpl implements PermissionContext {
    // This is only used when security information is requested but the current view is unsecured.
    private static final String DEFAULT="DEFAULT";
    private static final Permission NOT_SECURED_DEFAULT = new Permission(DEFAULT, null,null);

    static {
        NOT_SECURED_DEFAULT.setCreate(true);
        NOT_SECURED_DEFAULT.setRead(true);
        NOT_SECURED_DEFAULT.setUpdate(true);
        NOT_SECURED_DEFAULT.setRead(true);
    }

    @Inject
    private ViewConfigResolver viewConfigResolver;
    @Inject
    private FacesContext facesContext;
    @Inject
    SubjectPermissionMapper subjectPermissionMapper;
    @Inject
    PermissionConverter permissionConverter;

    private Permission permission;
    private ViewConfigDescriptor viewConfigDescriptor;
    private boolean secured;
    private boolean allowed;

    private static final Logger log = LoggerFactory.getLogger(PermissionContextImpl.class);

    @PostConstruct
    private void init() {
        viewConfigDescriptor = viewConfigResolver.getViewConfigDescriptor(facesContext.getViewRoot().getViewId());
        secured = viewConfigDescriptor.getCallbackDescriptor(Secured.class) != null;

        if (secured) {
            if (hasPermission()) {
                allowed = true;
            }
        }


        else if (secured == false) {
            permission = NOT_SECURED_DEFAULT;
            allowed = true;
            log.info("PermissionContextImpl created but the page was not secured. Page: {}", viewConfigDescriptor.getViewId());
        }
    }

    private boolean hasPermission() {
        permission = (Permission) subjectPermissionMapper.getPermission(permissionConverter.getPermissionId(viewConfigDescriptor.getConfigClass()));
        return permission != SubjectPermissionMapper.NOT_FOUND;
    }

    @Override
    public boolean isSecured() {
        return secured;
    }

    @Override
    public boolean hasCreate() {
        return permission.hasCreate();
    }

    @Override
    public boolean hasRead() {
        return permission.hasRead();
    }

    @Override
    public boolean hasUpdate() {
        return permission.hasUpdate();
    }

    @Override
    public boolean hasDelete() {
        return false;
    }

    @Override
    public boolean hasPartPermission(final String stringRepresentation) {
        return subjectPermissionMapper.getPermission(stringRepresentation) != null;
    }

    @Override
    public boolean hasCreate(final String id) {
        Permission p = (Permission) subjectPermissionMapper.getPermission(id);
        return p.hasCreate();
    }

    @Override
    public boolean hasRead(final String partPermission) {
        return false;
    }

    @Override
    public boolean hasUpdate(final String id) {
        Permission p = (Permission) subjectPermissionMapper.getPermission(id);
        return p.hasUpdate();    }

    @Override
    public boolean hasDelete(final String id) {
        Permission p = (Permission) subjectPermissionMapper.getPermission(id);
        return p.hasDelete();    }

    @Override
    public boolean isAllowed() {
        return allowed;
    }
}