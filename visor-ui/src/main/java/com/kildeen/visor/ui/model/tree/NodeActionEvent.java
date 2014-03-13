package com.kildeen.visor.ui.model.tree;

import com.kildeen.visor.core.api.permission.Permission;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-03-13
 */
public class NodeActionEvent {
    Permission permission;

    NodeActionEvent(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }
}
