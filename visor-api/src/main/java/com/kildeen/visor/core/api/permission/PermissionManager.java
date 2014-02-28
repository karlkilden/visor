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

    Collection<String> serialize(Collection<Permission> permissions);

    Set<Permission> deSerialize(Collection<String> permissions);
}
