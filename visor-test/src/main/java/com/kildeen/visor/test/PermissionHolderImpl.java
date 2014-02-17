package com.kildeen.visor.test;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.context.PermissionHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public class PermissionHolderImpl implements PermissionHolder {
    @Override
    public Set<Permission> getPermissions() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission("kildeen.visor.test.HelloWorld", ""));
        return permissions;
    }
}
