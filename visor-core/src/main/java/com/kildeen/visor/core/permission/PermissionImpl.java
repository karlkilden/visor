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

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.SubPermission;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

/**
 * <p>File created: 2014-02-16 15:25</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class PermissionImpl implements Permission {
    private int t = 0;
    private String id;
    private String path;
    private Set<PermissionImpl> children = new ListOrderedSet<>();
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;
    private boolean group;
    private int count = 1;

/*    public PermissionImpl(final String id, final Set<Permission> children, final ConfigDescriptor configDescriptor) {
        //We want no trouble with Json so we cheat here.
        this.children = (Set) children;
        this.id = id;
        if (configDescriptor != null) {
            this.path = configDescriptor.getPath();
            if (configDescriptor instanceof ViewConfigDescriptor) {
            } else {
                group = true;
            }
        }
        if (children != null) {
            count = count + children.size();
            for (Permission p : children) {
                count = count + p.getChildren().size();
            }

        }
    }*/


    @Override
    public boolean isParent() {
        return children.isEmpty() == false;
    }

    public PermissionImpl(PermissionModel permission) {
        for (PermissionModel permissionModel : permission.getChildren()) {
            this.children.add(new PermissionImpl(permissionModel));
        }
        this.id = permission.getId();
        this.path = permission.getPath();
        this.group = permission.getGroup();
        this.count = permission.getCount();
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
    public void pushCreate(final boolean create) {
        updateState(SubPermission.CREATE, create);
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
    public void pushRead(final boolean read) {
        updateState(SubPermission.READ, read);
    }

    @Override
    public boolean hasUpdate() {
        return update;
    }

    @Override
    public void pushUpdate(final boolean update) {
        updateState(SubPermission.UPDATE, update);
    }

    @Override
    public void setUpdate(final boolean update) {
        updateState(SubPermission.UPDATE, update);
    }

    @Override
    public boolean hasDelete() {
        return delete;
    }

    @Override
    public void setDelete(final boolean delete) {
        updateState(SubPermission.DELETE, delete);
    }

    @Override
    public void pushDelete(final boolean delete) {
        updateState(SubPermission.DELETE, delete);
    }

    @Override
    public void push(final SubPermission subPermission, final boolean state) {
        updateState(subPermission, state);
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
    public int getCount() {
        return count;
    }

    @Override
    public void privilege() {
        updateState(SubPermission.CREATE, true);
        updateState(SubPermission.READ, true);
        updateState(SubPermission.UPDATE, true);
        updateState(SubPermission.DELETE, true);
    }

    @Override
    public Set<Permission> getChildren() {
        return (Set) children;
    }

    private void updateState(SubPermission crud, boolean state) {
        if (group) {
            for (PermissionImpl permission : children) {
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
                delete = state;
        }
    }

    @Override
    public boolean equals(Object permissionModel) {
        if (this == permissionModel) {
            return true;
        }
        PermissionImpl permission = (PermissionImpl) permissionModel;
        return this.id.equals(permission.id);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 7).append(id).build();
    }

    @Override
    public String toString() {
      return "id: " +id + " path: "+path+ ". CRUD: "+ create + read + update + delete + " isGroup: " + isGroup() + " Count: "+ count;
    }

}


