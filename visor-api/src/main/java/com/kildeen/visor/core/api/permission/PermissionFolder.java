package com.kildeen.visor.core.api.permission;

import java.util.Collection;
import java.util.List;

/**
 * <p>File created: 2014-02-17 19:52</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class PermissionFolder implements PermissionModel {
    private List<PermissionModel> permissionsModels;
    private String folderName;
    private String path;

    public List<PermissionModel> getPermissions () {
        return permissionsModels;
    }

    public String getFolderName() {
        return folderName;
    }

    public PermissionFolder(final List<PermissionModel> permissionsModels, String folderName, String path) {
        this.permissionsModels = permissionsModels;
        this.folderName = folderName;
    }

    @Override
    public String getId() {
        return folderName;
    }

    @Override
    public Collection<PermissionModel> getAllChildren() {
        return permissionsModels;
    }

    @Override
    public boolean hasChildren() {
        return permissionsModels != null && !permissionsModels.isEmpty();
    }
}
