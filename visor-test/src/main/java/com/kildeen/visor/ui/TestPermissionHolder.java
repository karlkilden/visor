package com.kildeen.visor.ui;

import com.kildeen.visor.core.api.context.DefaultPermissionHolder;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import com.kildeen.visor.ui.model.PermissionBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-04-30
 */
@SessionScoped
@Specializes
public class TestPermissionHolder extends DefaultPermissionHolder {

    @Inject
    PermissionBean permissionBean;

    @Inject
    private PermissionDataHolderImpl permissionDataHolder;
    @Inject
    private PermissionResolver permissionResolver;

    @Override
    public Set<Permission> getPermissions() {

        if (permissionBean.getSelected().isEmpty()) {
            init();
        }

        return permissionBean.getSelected();
    }

    @PostConstruct
    private void init() {
        permissionBean.getSelected().add(permissionResolver.getPermission(HelloWorld.class));
    }
}
