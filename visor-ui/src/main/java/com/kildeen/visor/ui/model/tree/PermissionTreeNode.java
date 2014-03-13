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

package com.kildeen.visor.ui.model.tree;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.SubPermission;

/**
 * <p>File created: 2014-03-02 16:01</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class PermissionTreeNode {

    private String label;
    private Permission permission;
    private SubPermission subPermission;
    private int id;

    public boolean isSubPermission() {
        return subPermission != null;
    }

    public String getLabel() {
        return label;
    }

    public Permission getPermission() {
        return permission;
    }

    public SubPermission getSubPermission() {
        return subPermission;
    }

    public PermissionTreeNode(final SubPermission subPermission, final Permission permission) {
        this.subPermission = subPermission;
        this.permission = permission;
        this.label = subPermission.toString();
        this.id = id;
    }

    public PermissionTreeNode(final Permission permission) {
        this.permission = permission;
        this.label = permission.getId();
        this.id = id;
    }

    public PermissionTreeNode() {
    }
}
