package com.kildeen.visor.core.api.permission;

import com.kildeen.visor.core.api.permission.Permission;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-21
 */
public interface PermissionManager extends Serializable {

    public Collection<String> permissionToString(Collection<Permission> permissions);

    public Set<Permission> permissionFromString(Collection<String> permissions);
}
