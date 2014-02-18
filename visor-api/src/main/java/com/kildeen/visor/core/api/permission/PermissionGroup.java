package com.kildeen.visor.core.api.permission;

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

    public PermissionGroup(final String id, final Set<PermissionModel> children, final ConfigDescriptor configDescriptor) {
        super(id, children, configDescriptor);
    }
}
