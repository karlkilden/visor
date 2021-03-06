/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
        permissionContext.has("secured.Part1");
    }


    @Test
    public void nested_security_should_be_to_a_nested_group_hierarchy() throws Exception {
        Permission model = permissionResolver.getPermission("kildeen.mock.provided.Pages");
        assertEquals(PermissionImpl.class, model.getClass());
        assertTrue(model.isParent());
        Permission expectedChild = permissionResolver.getPermission("kildeen.mock.provided.Pages.Secured");
        expectedChild.equals(model.getChildren().iterator().next());
        assertTrue(model.getChildren().contains(expectedChild));
        expectedChild = permissionResolver.getPermission("kildeen.mock.provided.Pages.NestedSecured");
        Permission expectedGrandChild = permissionResolver.getPermission("kildeen.mock.provided.Pages.NestedSecured.NestedSecuredChild");
        assertTrue(expectedChild.getChildren().contains(expectedGrandChild));
    }



    @Test
    public void all_representations_must_be_equal() {
        List<String> ids = new ArrayList<>();
        for (ConfigDescriptor configDescriptor : configResolver.getConfigDescriptors()) {
            //We know that getId and getPermission is the same
            PermissionMappingContext context = new PermissionMappingContext(configResolver.getConfigDescriptors());
            if (context.isSecured(configDescriptor)) {
                ids.add(permissionConverter.getId(configDescriptor.getConfigClass()));
            }
        }
        assertEquals(ids.size(), permissionResolver.getPermissions().size());

            for (Permission model : permissionResolver.getPermissions()) {
                if (ids.contains(model.getId())) {
                    // OK
                }
                else {
                    fail("Id is missing from the mapped Models");
                }
            }
        for (Permission root : permissionResolver.getRootPermissions()) {
            validateExistence(root);
        }

        for (Permission permission : permissionResolver.getPermissions()) {
            validateExistence(permission);
        }

    }

    private void validateExistence(Permission root) {
        assertEquals(root.getId(), permissionResolver.getPermission(root.getId()).getId());
        for (Permission child : root.getChildren()) {
            validateExistence(child);
        }
    }
}
