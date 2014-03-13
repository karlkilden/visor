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
import com.kildeen.visor.core.api.permission.PermissionResolver;
import com.kildeen.visor.core.permission.PermissionModel;
import org.omnifaces.model.tree.ListTreeModel;
import org.omnifaces.model.tree.TreeModel;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>File created: 2014-03-01 22:45</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@SessionScoped
@Named
public class TreeBean implements Serializable {

    @Inject
    private PermissionResolver permissionResolver;

    @Inject
    private Event<NodeActionEvent> event;

    private static final Logger log = LoggerFactory.getLogger(TreeBean.class);

    private TreeModel<Permission> tree;
    private String currentNodeId;
    private List<Permission> rootPermissions;
    private Set<Permission> selected = new HashSet<>();
    private Permission currentPermission;

    @PostConstruct
    public void init() {

        rootPermissions = permissionResolver.getRootPermissions();
        tree = new ListTreeModel<>();
        for (Permission p : rootPermissions) {
            TreeModel<Permission> node = tree.addChild(p);
            addChildren(node);
        }
    }

    private TreeModel<Permission> addChildren(TreeModel<Permission> model) {
        for (Permission child : model.getData().getChildren()) {
            addChildren(model.addChild(child));
        }
        return model;
    }

    public Permission getCurrentPermission() {
        return currentPermission;
    }

    public TreeModel<Permission> getTree() {
        return tree;
    }

    public void update() {
        currentNodeId = Faces.getRequestParameter("nodeId");
        currentPermission = PermissionTreeUtil.getPermission(tree, currentNodeId);
        send(currentPermission);
    }

    public void onCheckedNode() {
        String node = Faces.getRequestParameter("checkedNodeId");
        Permission currentPermission = PermissionTreeUtil.getPermission(tree, node);
        selected.add(currentPermission);
        send(currentPermission);
    }

    public void onUncheckedNode() {
        String node = Faces.getRequestParameter("uncheckedNodeId");
        Permission currentPermission = PermissionTreeUtil.getPermission(tree, node);
        selected.remove(currentPermission);
        send(currentPermission);

    }

    private void send(Permission permission) {
        event.fire(new NodeActionEvent(permission));
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public void setSelected(Set<Permission> selected) {
        this.selected = selected;
    }
}
