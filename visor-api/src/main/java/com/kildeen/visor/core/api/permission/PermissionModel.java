package com.kildeen.visor.core.api.permission;

import java.util.Collection;

/**
 * <p>File created: 2014-02-17 22:46</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public interface PermissionModel {
    public String getId();
    public Collection<PermissionModel> getAllChildren();
}
