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


    boolean isParent();

    boolean hasCreate();

    void setCreate(boolean create);

    boolean hasRead();

    void setRead(boolean read);

    boolean hasUpdate();

    void setUpdate(boolean update);

    boolean hasDelete();

    void setDelete(boolean delete);

    boolean isPrivileged();

    @Override
    int hashCode();

    @Override
    boolean equals(Object permissionModel);

    public Set<PermissionModel> getChildren();

    void privilege();

    String getId();

    boolean isCreate();

    boolean isRead();

    boolean isUpdate();

    boolean isDelete();

    String getPath();
}
