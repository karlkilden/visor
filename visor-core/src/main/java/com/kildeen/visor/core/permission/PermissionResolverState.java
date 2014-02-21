package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionImpl;
import org.apache.commons.collections4.set.ListOrderedSet;

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
    private ListOrderedSet<PermissionImpl> permissions = new ListOrderedSet<>();
    private ListOrderedSet<Permission> rootPermissions = new ListOrderedSet<>();
    private Map<String, Permission> mappedPermissionModels = new HashMap<>();

    public PermissionResolverState(PermissionMappingContext mappingContext) {

        this.mappingContext = mappingContext;
    }

    private void addToRelevantCollections(PermissionImpl permission, boolean isRoot) {
        if (permission instanceof PermissionImpl) {
            permissions.add((PermissionImpl) permission);

        }
        if (isRoot) {
            rootPermissions.add(permission);
        }
        permissions.add(permission);
        mappedPermissionModels.put(permission.getId(), permission);

    }

    public List<PermissionImpl> getPermissions() {
        return permissions.asList();
    }

    public List<Permission> getRootPermissions() {
        return rootPermissions.asList();
    }

    public Map<String, Permission> getMappedPermissionModels() {
        return mappedPermissionModels;
    }

    public void add(PermissionImpl permission, boolean isRoot) {
        addToRelevantCollections(permission, isRoot);
    }
}
