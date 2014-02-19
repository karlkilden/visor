package com.kildeen.visor.core.api.permission;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;

import java.util.Set;

/**
 * <p>File created: 2014-02-17 22:46</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public abstract class AbstractPermissionModel implements PermissionModel {
    protected transient ConfigDescriptor configDescriptor;
    protected String id;
    protected Set<PermissionModel> children = new ListOrderedSet<>();


    protected AbstractPermissionModel(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        this.children = children;
        this.id = id;
        this.configDescriptor = configDescriptor;
    }

    protected AbstractPermissionModel() {
    }

    @Override
    public String getPath() {
        return configDescriptor.getPath();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Set<PermissionModel> getChildren() {
        return children;
    }

    protected void setChildren(final Set<PermissionModel> children) {
        this.children = children;
    }

    @Override
    public boolean equals (Object permissionModel) {
        if (this == permissionModel) {
            return true;
        }
        if (this.getClass() != permissionModel.getClass()) {
            return false;
        }

        PermissionModel model = (PermissionModel) permissionModel;
        return this.id.equals(model.getId());
    }
}
