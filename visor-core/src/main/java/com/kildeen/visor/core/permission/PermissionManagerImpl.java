package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;
import com.kildeen.visor.core.api.permission.PermissionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-21
 */
@ApplicationScoped
public class PermissionManagerImpl implements PermissionManager {

    @Inject
    private PermissionConverter permissionConverter;

    @Override
    public Collection<String> serialize(Collection<Permission> permissions) {
        return permissionConverter.serializeAll(permissions);
    }

    @Override
    public Set<Permission> deSerialize(Collection<String> permissions) {
        return permissionConverter.deserializeAll(permissions);
    }
}
