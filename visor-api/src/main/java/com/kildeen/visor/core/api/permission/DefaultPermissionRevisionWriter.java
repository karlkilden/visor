package com.kildeen.visor.core.api.permission;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class DefaultPermissionRevisionWriter implements PermissionRevisionWriter {
    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public boolean isActivated() {
        return false;
    }
}
