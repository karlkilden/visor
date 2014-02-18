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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>File created: 2014-02-16 15:25</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class Permission implements PermissionModel {

    private String masterPermission;
    private Set<Permission> children = new HashSet<>();
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
    private String viewId;

    public Permission(String masterPermission, String viewId) {
        this.masterPermission = masterPermission;
        this.viewId = viewId;
    }

    public boolean hasCreate() {
        return create;
    }

    public void setCreate(final boolean create) {
        this.create = create;
    }

    public boolean hasRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public boolean gasUpdate() {
        return update;
    }

    public void setUpdate(final boolean update) {
        this.update = update;
    }

    public boolean hasDelete() {
        return delete;
    }

    public void setDelete(final boolean delete) {
        this.delete = delete;
    }

    public boolean isPrivileged() {
        return create && read && update && delete;
    }


    public String getMasterPermission() {
        return masterPermission;
    }

    public Set<Permission> getChildren() {
        return children;
    }

    public boolean hasUpdate() {
        return update;
    }

    public String getViewId() {
        return viewId;
    }

    public Permission (Permission masterPermission) {
        this.masterPermission = masterPermission.masterPermission;
        this.children = masterPermission.children;
        this.create = masterPermission.create;
        this.read = masterPermission.read;
        this.update = masterPermission.update;
        this.delete = masterPermission.delete;
        this.viewId = masterPermission.viewId;
    }

    @Override
    public String getId() {
        return masterPermission;
    }

    @Override
    public Collection<PermissionModel> getAllChildren() {
        Collection c =  children;
        return c;
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
