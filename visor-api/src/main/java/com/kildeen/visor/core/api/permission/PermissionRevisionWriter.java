package com.kildeen.visor.core.api.permission;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-21
 */
public interface PermissionRevisionWriter extends Serializable {
    public int getVersion();
    public boolean isActivated();
}
