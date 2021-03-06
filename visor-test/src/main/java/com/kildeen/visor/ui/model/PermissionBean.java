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

package com.kildeen.visor.ui.model;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import com.kildeen.visor.ui.model.tree.TreeContext;
import org.omnifaces.model.tree.TreeModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
public class PermissionBean implements Serializable {

    @Inject
    private PermissionResolver permissionResolver;
    private TreeModel<Permission> tree;
    private Set<Permission> selected = new HashSet<>();

    @Inject
    TreeContext treeContext;

    @PostConstruct
    public void init() {
        tree = treeContext.getDefaultTree();
    }

    public TreeModel<Permission> getTree() {
        return tree;
    }

    public void save() {
        selected = treeContext.getSelected();
    }

    public Set<Permission> getSelected() {
        return selected;
    }
}
