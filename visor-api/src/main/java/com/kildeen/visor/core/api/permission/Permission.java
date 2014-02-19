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

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;

import java.util.Set;

/**
 * <p>File created: 2014-02-16 15:25</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class Permission implements PermissionModel {
    private int t = 0;
    protected transient ConfigDescriptor configDescriptor;
    protected String id;
    protected Set<Permission> children = new ListOrderedSet<>();
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;

    public Permission(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        //We want no trouble with Json so we cheat here.
        this.children = (Set) children;
        this.id = id;
        this.configDescriptor = configDescriptor;
    }

    public Permission() {

    }

    @Override
    public boolean isParent() {
        return children.isEmpty() == false;
    }

    public Permission(Permission permission) {
        this.children = (Set) permission.children;
        this.id = permission.id;
        this.configDescriptor = permission.configDescriptor;
        this.create = permission.create;
        this.read = permission.create;
        this.update = permission.create;
        this.delete = permission.create;
    }

    @Override
    public boolean hasCreate() {
        return create;
    }

    @Override
    public void setCreate(final boolean create) {
        this.create = create;
    }

    @Override
    public boolean hasRead() {
        return read;
    }

    @Override
    public void setRead(final boolean read) {
        this.read = read;
    }

    @Override
    public boolean hasUpdate() {
        return update;
    }

    @Override
    public void setUpdate(final boolean update) {
        this.update = update;
    }

    @Override
    public boolean hasDelete() {
        return delete;
    }

    @Override
    public void setDelete(final boolean delete) {
        this.delete = delete;
    }

    @Override
    public boolean isPrivileged() {
        return create && read && update && delete;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isCreate() {
        return create;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public boolean isUpdate() {
        return update;
    }

    @Override
    public boolean isDelete() {
        return delete;
    }

    @Override
    public String getPath() {
        return configDescriptor.getPath();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 7).append(id).append(t).
                append(create).append(read).append(update).append(delete).build();
    }

    @Override
    public boolean equals(Object permissionModel) {
        if (super.equals(permissionModel)) {
            Permission permission = (Permission) permissionModel;
            return this.create == permission.create && this.read == permission.read && this.update == permission.update
                    && this.delete == permission.delete;
        }
        return false;
    }

    @Override
    public void privilege() {
        create = true;
        read = true;
        update = true;
        delete = true;
    }

    @Override
    public Set<PermissionModel> getChildren() {
        return (Set)children;
    }
}
