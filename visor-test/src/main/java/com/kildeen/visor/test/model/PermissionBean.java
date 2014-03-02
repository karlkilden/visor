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

package com.kildeen.visor.test.model;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import com.kildeen.visor.core.api.permission.SubPermission;
import org.omnifaces.model.tree.ListTreeModel;
import org.omnifaces.model.tree.TreeModel;
import org.primefaces.component.tree.Tree;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>File created: 2014-03-01 22:45</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
@Named
public class PermissionBean implements Serializable {

    @Inject
    private PermissionResolver permissionResolver;
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private Map<CheckboxTreeNode, CheckboxTreeNode> parent = new HashMap<>();
    private List<String> update = new ArrayList<>();
    private TreeModel<Permission> tree;

    @PostConstruct
    public void init() {

        tree = new ListTreeModel<>();
        for (Permission p :permissionResolver.getRootPermissions()) {
            addChildren(tree.addChild(p));
        }


/*
        createTree();
*/
    }
    private TreeModel addChildren (TreeModel model) {
        for (model.)
    }
    private void createTree() {
        root = new CheckboxTreeNode(new PermissionTreeNode(), null);
        for (Permission p : permissionResolver.getRootPermissions()) {
            CheckboxTreeNode node = new CheckboxTreeNode(new PermissionTreeNode(p), root);
            addSubPermissions(p, node);
            createNode(p, node);
        }
    }


    private void createNode(Permission permission, TreeNode parent) {
        for (Permission child : permission.getChildren()) {
            CheckboxTreeNode childNode = new CheckboxTreeNode(new PermissionTreeNode(child), parent);
            addSubPermissions(child, childNode);
            createNode(child, childNode);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(final TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        PermissionTreeNode permissionTreeNode = (PermissionTreeNode) event.getTreeNode().getData();
        TreeNode node = event.getTreeNode();
        setExpand(node);
        if (permissionTreeNode.isSubPermission() == false) {
            return;
        }
        permissionTreeNode.getPermission().push(permissionTreeNode.getSubPermission(), true);
        handleUpdate(event.getComponent().getParent(), permissionTreeNode.getSubPermission().ordinal());
        permissionTreeNode.getPermission().push(permissionTreeNode.getSubPermission(), true);
        handleEvent(event.getTreeNode().getParent(), permissionTreeNode.getSubPermission(), true);
        RequestContext ctx = RequestContext.getCurrentInstance();
        ctx.update(update);
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        Tree tree = (Tree) event.getComponent();
        tree.getRowKey();
        PermissionTreeNode permissionTreeNode = (PermissionTreeNode) event.getTreeNode().getData();
        TreeNode node = event.getTreeNode();
        setExpand(node);
        if (permissionTreeNode.isSubPermission() == false) {
            return;
        }
        handleUpdate(event.getComponent().getParent(), permissionTreeNode.getSubPermission().ordinal());
        permissionTreeNode.getPermission().push(permissionTreeNode.getSubPermission(), false);
        handleEvent(event.getTreeNode().getParent(), permissionTreeNode.getSubPermission(), false);
        RequestContext ctx = RequestContext.getCurrentInstance();
        ctx.update(update);
    }

    private void handleUpdate(final UIComponent parent, int subPermission) {
        update.add(parent.getChildren().get(subPermission).getId());

        for (int i = 3; 3 < parent.getChildren().size(); i++) {
             handleUpdate(parent.getChildren().get(i), subPermission);
        }
    }

    private void setExpand(TreeNode node) {
/*            node.setExpanded(true);
        while (node.getParent() != null) {
            node.getParent().setExpanded(true);
            node = node.getParent();
        }*/
    }

    private void handleEvent(TreeNode treeNode, SubPermission subPermission, boolean state) {
        PermissionTreeNode permissionTreeNode = (PermissionTreeNode) treeNode.getData();
        for (TreeNode childNode : treeNode.getChildren()) {
            PermissionTreeNode child = (PermissionTreeNode) childNode.getData();
            if (child.isSubPermission() && child.getSubPermission().equals(subPermission)) {
                childNode.setSelected(state);
            } else if (child.isSubPermission() == false) {
                handleEvent(childNode, subPermission, state);
            }
        }
    }

    private void addSubPermissions(final Permission p, final CheckboxTreeNode node) {
        CheckboxTreeNode crud = new CheckboxTreeNode(new PermissionTreeNode(SubPermission.CREATE, p), node);
        parent.put(crud, node);
        crud = new CheckboxTreeNode(new PermissionTreeNode(SubPermission.READ, p), node);
        parent.put(crud, node);
        crud = new CheckboxTreeNode(new PermissionTreeNode(SubPermission.UPDATE, p), node);
        parent.put(crud, node);
        crud = new CheckboxTreeNode(new PermissionTreeNode(SubPermission.DELETE, p), node);
        parent.put(crud, node);

    }


    public void onNodeExpand(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        event.getTreeNode().setExpanded(false);
    }

    public List<Permission> getPermissionResolver() {
        return permissionResolver.getRootPermissions();
    }
}
