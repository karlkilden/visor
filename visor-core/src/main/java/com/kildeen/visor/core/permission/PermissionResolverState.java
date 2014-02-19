package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class PermissionResolverState {
    private final PermissionMappingContext mappingContext;
    private List<Permission> permissions = new ArrayList<>();
    private List<PermissionModel> permissionModels = new ArrayList<>();
    private List<PermissionModel> rootPermissionModels = new ArrayList<>();
    private Map<String, PermissionModel> mappedPermissionModels = new HashMap<>();

    public PermissionResolverState(PermissionMappingContext mappingContext) {

        this.mappingContext = mappingContext;
    }

    private void addToRelevantCollections(PermissionModel permissionModel, boolean isRoot) {
        if (permissionModel instanceof Permission) {
            permissions.add((Permission) permissionModel);

        }
        if (isRoot) {
            rootPermissionModels.add(permissionModel);
        }
        permissionModels.add(permissionModel);
        mappedPermissionModels.put(permissionModel.getId(), permissionModel);

    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public List<PermissionModel> getPermissionModels() {
        return permissionModels;
    }

    public List<PermissionModel> getRootPermissionModels() {
        return rootPermissionModels;
    }

    public Map<String, PermissionModel> getMappedPermissionModels() {
        return mappedPermissionModels;
    }

    public void add(PermissionModel permissionModel, boolean isRoot) {
        addToRelevantCollections(permissionModel, isRoot);
    }
}
