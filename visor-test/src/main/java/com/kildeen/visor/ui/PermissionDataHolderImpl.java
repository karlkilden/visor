package com.kildeen.visor.ui;

import com.kildeen.visor.core.api.context.PermissionDataHolder;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import org.apache.commons.collections4.set.ListOrderedSet;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public class PermissionDataHolderImpl implements PermissionDataHolder {
    @Inject
    private PermissionConverter permissionConverter;

    @Inject
    private PermissionResolver permissionResolver;

    @Override
    public Set<String> getPermissions() {
        Set<String> permissions = new ListOrderedSet<>();
        Permission p = permissionResolver.getPermission(HelloWorld.class);
        permissions.add(permissionConverter.serialize(p));

        return permissions;
    }
}
