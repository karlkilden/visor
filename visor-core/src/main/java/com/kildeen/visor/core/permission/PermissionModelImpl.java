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

import org.apache.commons.collections4.set.ListOrderedSet;
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
public class PermissionModelImpl implements PermissionModel {
    private int t = 0;
    private String id;
    private String path;
    private Set<PermissionModel> children = new ListOrderedSet<>();
    private boolean group;
    private int count = 1;

    public PermissionModelImpl(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        if (children != null)   {
            this.children = children;
        }
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
            for (PermissionModel p : children) {
                count = count + p.getChildren().size();
            }

        }
    }

    public PermissionModelImpl(PermissionModelImpl permission) {
        this.children = (Set) permission.children;
        this.id = permission.id;
        this.path = permission.path;
        this.group = permission.group;
        this.count = permission.count;
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
    public Set<PermissionModel> getChildren() {
        return (Set) children;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean getGroup() {
        return group;
    }
}


