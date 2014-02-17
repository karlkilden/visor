package com.kildeen.visor.core.api.permission;

import java.util.List;

/**
 * <p>File created: 2014-02-17 19:52</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class PermissionFolder implements PermissionModel {
    private List<Permission> permissions;
    private String folderName;

    public List<Permission> getPermissions () {
        return permissions;
    }

    public String getFolderName() {
        return folderName;
    }

    public PermissionFolder(final List<Permission> permissions, String folderName) {
        this.permissions = permissions;
        this.folderName = folderName;
    }

    @Override
    public String getId() {
        return folderName;
    }
}
