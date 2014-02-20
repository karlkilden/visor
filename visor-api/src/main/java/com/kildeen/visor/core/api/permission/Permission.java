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
import org.apache.commons.lang3.builder.EqualsBuilder;
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
    private String id;
    private String path;
    private Set<Permission> children = new ListOrderedSet<>();
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
    private boolean group;

    public Permission(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        //We want no trouble with Json so we cheat here.
        this.children = (Set) children;
        this.id = id;
        if (configDescriptor != null) {
            this.path = configDescriptor.getPath();
            if (configDescriptor instanceof ViewConfigDescriptor) {
            }
            else {
                group = true;
            }
        }
    }


    @Override
    public boolean isParent() {
        return children.isEmpty() == false;
    }

    public Permission(Permission permission) {
        this.children = (Set) permission.children;
        this.id = permission.id;
        this.create = permission.create;
        this.read = permission.create;
        this.update = permission.create;
        this.delete = permission.create;
        this.path = permission.path;
        this.group = permission.group;
    }

    @Override
    public boolean hasCreate() {
        return create;
    }

    @Override
    public void setCreate(final boolean create) {
        updateState(Crud.CREATE, create);
    }

    @Override
    public boolean hasRead() {
        return read;
    }

    @Override
    public void setRead(final boolean read) {
        updateState(Crud.READ, read);
    }

    @Override
    public boolean hasUpdate() {
        return update;
    }

    @Override
    public void setUpdate(final boolean update) {
        updateState(Crud.UPDATE, update);
    }

    @Override
    public boolean hasDelete() {
        return delete;
    }

    @Override
    public void setDelete(final boolean delete) {
        updateState(Crud.DELETE, delete);
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
        return path;
    }

    @Override
    public boolean isGroup() {
        return group;
    }

    @Override
    public void privilege() {
        updateState(Crud.CREATE, true);
        updateState(Crud.READ, true);
        updateState(Crud.UPDATE, true);
        updateState(Crud.DELETE, true);


    }

    @Override
    public Set<PermissionModel> getChildren() {
        return (Set) children;
    }

    private void updateState(Crud crud, boolean state) {
        if (group) {
            for (Permission permission : children) {
                permission.updateState(crud, state);
            }
        }
        switch (crud) {
            case CREATE:
                create = state;
            case READ:
                read = state;
            case UPDATE:
                update = state;
            case DELETE:
                delete = state ;
        }
    }

    @Override
    public boolean equals(Object permissionModel) {
        Permission permission = (Permission) permissionModel;
        return new EqualsBuilder().append(this.id, permission.id).append(this.create,
                permission.create).append(this.read, permission.read).append(this.update,
                permission.update).append(this.delete, permission.delete).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 7).append(id).
                append(create).append(read).append(update).append(delete).build();
    }

    private enum Crud {
        CREATE, READ, UPDATE, DELETE;
    }
}


