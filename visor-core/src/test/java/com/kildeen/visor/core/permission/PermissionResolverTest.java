package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.MockFacesContext;
import com.kildeen.visor.core.api.context.PermissionContext;
import com.kildeen.visor.core.api.permission.*;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

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
    private ViewConfigResolver configResolver;

    @Inject
    MockFacesContext mockFacesContext;
    private List<Permission> permissions;
    @Inject
    private PermissionContext permissionContext;

    @Inject
    PermissionConverter permissionConverter;

    @Before
    public void setUp() throws Exception {
        permissions = permissionResolver.getPermissions();
    }

    @Test
    public void get_permissions_should_return_all_secured_view_configs() {

        boolean nestedExists = false;
        boolean adminExists = false;
        for (Permission p : permissions) {
            if (p.getPath().equals("/pages/secured.xhtml")) {
                nestedExists = true;
            }

            if (p.getPath().equals("/admin.xhtml")) {
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
    public void nested_security_should_be_to_a_nested_group_hierarchy() throws Exception {
        PermissionModel model = permissionResolver.getPermissionModel("kildeen.mock.provided.Pages");
        assertEquals(PermissionGroup.class, model.getClass());
        assertTrue(model.isParent());
        PermissionModel expectedChild = permissionResolver.getPermissionModel("kildeen.mock.provided.Pages.Secured");
        assertTrue(model.getChildren().contains(expectedChild));
        expectedChild = permissionResolver.getPermissionModel("kildeen.mock.provided.Pages.NestedSecured");
        PermissionModel expectedGrandChild = permissionResolver.getPermissionModel("kildeen.mock.provided.Pages.NestedSecured.NestedSecuredChild");
        assertTrue(expectedChild.getChildren().contains(expectedGrandChild));
    }

    @Test
    public void all_groups_must_have_children() {
        for (PermissionModel model : permissionResolver.getPermissionModels()) {
            if (model instanceof PermissionGroup) {
                if (model.getChildren().isEmpty()) {
                    fail("Found a Group with no children. It is illegal, it should have been a Permission not a PermissionGroup");
                }
            }
        }
    }

    @Test
    public void all_representations_must_be_equal() {
        List<String> ids = new ArrayList<>();
        for (ConfigDescriptor configDescriptor : configResolver.getConfigDescriptors()) {
            //We know that getPermissionId and getPermissionGroup is the same
            ids.add(permissionConverter.getPermissionId(configDescriptor.getConfigClass()));
        }
        assertEquals(ids.size(), permissionResolver.getPermissionModels().size());
    }
}
