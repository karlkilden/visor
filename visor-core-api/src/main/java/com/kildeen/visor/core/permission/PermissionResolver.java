package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.Permission;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public interface PermissionResolver extends Serializable {
    public List<Permission> getPermissions();
    public void boot();
}
