package com.kildeen.visor.core.api.context;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-21
 */
@SessionScoped
public class DefaultPermissionHolder implements PermissionHolder {

    @Inject
    private PermissionConverter permissionConverter;

    @Inject
    private PermissionDataHolder permissionDataHolder;

    private Set<Permission> permissions;

    @PostConstruct
    private void init() {
        if (isAutomatic() && permissionDataHolder.getPermissions() != null) {
            permissions = permissionConverter.deserializeAll(permissionDataHolder.getPermissions());
        }
    }

    @Override
    public Set<Permission> getPermissions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }
}
