package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.MockFacesContext;
import com.kildeen.visor.core.api.context.PermissionContext;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
@RunWith(CdiTestRunner.class)
public class PermissionResolverTest {

    @Inject
    private PermissionResolver permissionResolver;
    @Inject
    MockFacesContext mockFacesContext;
    private List<Permission> permissions;
    @Inject
    private PermissionContext permissionContext;

    @Before
    public void setUp() throws Exception {
        permissions = permissionResolver.getPermissions();
    }

    @Test
    public void get_permissions_should_return_all_secured_view_configs() {

        boolean nestedExists = false;
        boolean adminExists = false;
        for (Permission p : permissions) {
            if (p.getViewId().equals("/pages/secured.xhtml")) {
                nestedExists = true;
            }

            if (p.getViewId().equals("/admin.xhtml")) {
                adminExists = true;
            }
        }

        assertTrue(nestedExists);
        assertTrue(adminExists);

    }

    @Test
    public void part_permissions_should_have_parent_simpleName_uncapitalized_simpleName_as_string_representation() throws Exception {
        mockFacesContext.setId("/pages/secured.xhtml");
        permissionContext.hasPartPermission("secured.Part1");
    }

    @Test
    public void permissionFolders_should_return_all_folders() throws Exception {
        assertNotNull(permissionResolver.getPermissionFolders());
        assertEquals("kildeen.mock.provided.Pages.Secured", permissionResolver.getPermissionFolders().get(0).getPermissions().get(0).getMasterPermission());
    }
}
