package com.kildeen.visor.test;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.context.PermissionHolder;
import com.kildeen.visor.core.api.permission.PermissionModel;
import org.apache.commons.collections4.set.ListOrderedSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public class PermissionHolderImpl implements PermissionHolder {
    @Override
    public Set<PermissionModel> getPermissions() {
        Set<PermissionModel> permissions = new ListOrderedSet<>();
        permissions.add(new Permission("kildeen.visor.test.HelloWorld", null, null));
        return permissions;
    }
}
