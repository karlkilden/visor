package com.kildeen.visor.core.api.permission;

import org.omg.CORBA.Request;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
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
public class TruncatedPermissionBuilder implements Serializable {

    @Inject
    private PermissionResolver resolver;

    @Inject
    private PermissionRevisionWriter permissionRevisionWriter;

    private List<String> truncatedPermissionModel = new ArrayList<>();

    public TruncatedPermissionModel map(Collection<PermissionModel> permissionModels) {

        for (PermissionModel model : permissionModels) {
            if (model instanceof Permission) {
                if (useShortHand((Permission) model)) {
                    Permission p = (Permission) model;
                    truncatedPermissionModel.add(model.getId());
                } else {
                    truncatedPermissionModel.add(model.getId() + getCrudInfo(model));
                    for (PermissionModel child : model.getChildren()) {
                        truncatedPermissionModel.add(child.getId() + getCrudInfo(child));
                    }
                }
            } else {
                if (useShortHand((PermissionGroup)model))  {
                    truncatedPermissionModel.add(model.getId());
                }

                map(model.getChildren());
            }
        }
        TruncatedPermissionModel truncatedModel = new TruncatedPermissionModel(truncatedPermissionModel, truncatedPermissionModel, permissionRevisionWriter.getVersion());
        return truncatedModel;
    }


    private boolean useShortHand(PermissionGroup permission) {
        PermissionGroup toMatch = (PermissionGroup) resolver.getPermissionModel(permission.getId());
        if (permission.getChildren().size() == toMatch.getChildren().size()) {
            boolean shorthand = false;
            for (PermissionModel child : permission.getChildren()) {
                if (child instanceof Permission) {
                    shorthand = useShortHand((Permission)child);
                    if (shorthand == false)   {
                        return false;
                    }
                } else {
                    shorthand = useShortHand((PermissionGroup)child);
                    if (shorthand == false)   {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean useShortHand(Permission permission) {
        Permission toMatch = (Permission) resolver.getPermissionModel(permission.getId());
        if (permission.getChildren().size() == toMatch.getChildren().size()) {
            for (PermissionModel child : permission.getChildren()) {
                Permission childPermission = (Permission) child;
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

    private Object getCrudInfo(PermissionModel model) {
        Permission p = (Permission) model;
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
