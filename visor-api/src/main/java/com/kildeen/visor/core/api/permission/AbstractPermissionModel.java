package com.kildeen.visor.core.api.permission;

import org.apache.commons.collections4.set.ListOrderedSet;
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
    protected boolean isParent;
    protected boolean isPartPermission;
    protected boolean isPermission;
    protected boolean isPermissionGroup;

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

    @Override
    public boolean isParent() {
        return isParent;
    }

    @Override
    public boolean isPartPermission() {
        return isPartPermission;
    }

    @Override
    public boolean isPermission() {
        return isPermission;
    }

    @Override
    public boolean isPermissionGroup() {
        return isPermissionGroup;
    }

    protected void setChildren(final Set<PermissionModel> children) {
        this.children = children;
    }
}
