package com.kildeen.visor.core.api.permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class MinimizedPermission {
    private int t=1;
    private int version;
    private List<String> truncatedPermissionModel = new ArrayList<>();

    public MinimizedPermission(List<String> truncatedPermissionModel, List<String> shortHands, int version) {
        this.version = version;
        this.truncatedPermissionModel = truncatedPermissionModel;
    }

    public int getVersion() {
        return version;
    }

    public List<String> getTruncatedPermissionModel() {
        return truncatedPermissionModel;
    }
}
