package com.kildeen.visor.core.api.permission;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
@ApplicationScoped
public class PermissionMinimizer implements Serializable {

    @Inject
    private PermissionResolver resolver;

    @Inject
    private PermissionRevisionWriter permissionRevisionWriter;

    private List<String> truncatedPermissionModel = new ArrayList<>();

    public MinimizedPermission minimize(Collection<Permission> permissions) {

        for (Permission model : permissions) {
            if (useShortHand(model)) {
                PermissionImpl p = (PermissionImpl) model;
                truncatedPermissionModel.add(model.getId());
            } else {
                truncatedPermissionModel.add(model.getId() + getCrudInfo(model));
                for (Permission child : model.getChildren()) {
                    truncatedPermissionModel.add(child.getId() + getCrudInfo(child));
                }
            }
        }
        MinimizedPermission truncatedModel = new MinimizedPermission(truncatedPermissionModel, truncatedPermissionModel, permissionRevisionWriter.getVersion());
        return truncatedModel;
    }


    private boolean useShortHand(Permission permission) {
        Permission toMatch = resolver.getPermissionModel(permission.getId());
        if (permission.getChildren().size() == toMatch.getChildren().size() && permission.isPrivileged()) {
            boolean shorthand = false;
            for (Permission child : permission.getChildren()) {
                if (child instanceof PermissionImpl) {
                    shorthand = useShortHand((PermissionImpl) child);
                    if (shorthand == false) {
                        return false;
                    }
                } else {
                    shorthand = useShortHand( child);
                    if (shorthand == false) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean useShortHand(PermissionImpl permission) {
        PermissionImpl toMatch = (PermissionImpl) resolver.getPermissionModel(permission.getId());
        if (permission.getChildren().size() == toMatch.getChildren().size()) {
            for (Permission child : permission.getChildren()) {
                PermissionImpl childPermission = (PermissionImpl) child;
                if (childPermission.isPrivileged()) {
                    //OK
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Object getCrudInfo(Permission model) {
        PermissionImpl p = (PermissionImpl) model;
        StringBuilder sb = new StringBuilder();
        sb.append("*");
        if (p.hasCreate()) {
            sb.append("C");
        }
        if (p.hasRead()) {
            sb.append("R");
        }
        if (p.hasUpdate()) {
            sb.append("U");
        }
        if (p.hasDelete()) {
            sb.append("D");
        }
        return sb.toString();
    }
}
