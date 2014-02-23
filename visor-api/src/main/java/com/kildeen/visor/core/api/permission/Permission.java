package com.kildeen.visor.core.api.permission;

import java.util.Set;

/**
 * <p>File created: 2014-02-18 20:08</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public interface Permission {

    boolean isParent();

    boolean hasCreate();

    void setCreate(boolean create);
    void pushCreate(boolean create);

    boolean hasRead();

    void setRead(boolean read);
    void pushRead(boolean read);


    boolean hasUpdate();

    void setUpdate(boolean update);
    void pushUpdate(boolean update);

    boolean hasDelete();

    void setDelete(boolean delete);
    void pushDelete(boolean delete);


    boolean isPrivileged();

    public Set<Permission> getChildren();

    boolean isGroup();

    int getCount();

    void privilege();

    String getId();

    boolean isCreate();

    boolean isRead();

    boolean isUpdate();

    boolean isDelete();

    String getPath();
}
