package com.kildeen.visor.core.api.permission;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-18
 */
public class PermissionTest {


    @Test
    public void permission_should_return_children_as_model() throws Exception {
        Permission permission = new Permission("", null,null);
        Permission child = new Permission("2", null,null);
        permission.getChildren().add(child);

        Assert.assertEquals(child, permission.getChildren().iterator().next());
    }
}
