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

import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>File created: 2014-02-16 15:25</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class Permission {

    private String permission;
    private Set<Permission> children = new HashSet<>();
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
    private String viewId;

    public Permission(String permission, String viewId) {
        this.permission = permission;
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


    public String getPermission() {
        return permission;
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

    public Permission (Permission permission) {
        this.permission = permission.permission;
        this.children = permission.children;
        this.create = permission.create;
        this.read = permission.read;
        this.update = permission.update;
        this.delete = permission.delete;
        this.viewId = permission.viewId;
    }
}
