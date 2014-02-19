package com.kildeen.visor.core.api.permission;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class TruncatedPermissionModel {
    private int version;
    private List<String> truncatedPermissionModel = new ArrayList<>();

    public TruncatedPermissionModel(List<String> truncatedPermissionModel, List<String> shortHands, int version) {
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
