package com.kildeen.visor.core.api.permission;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;

import java.util.Set;

/**
 * <p>File created: 2014-02-17 19:52</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class PermissionGroup extends AbstractPermissionModel {
    private int t = 1;
    public PermissionGroup(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        super(id, children, configDescriptor);
    }

    public PermissionGroup() {
    }

    @Override
    public boolean isParent() {
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13,17).append(id).append(t).build();
    }
}
