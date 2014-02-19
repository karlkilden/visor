package com.kildeen.visor.core.api.permission;

import java.util.Set;

/**
 * <p>File created: 2014-02-18 20:08</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public interface PermissionModel {
    String getId();

    String getPath();

    Set<PermissionModel> getChildren();

    boolean isParent();
}
