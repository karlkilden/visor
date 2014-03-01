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

import com.kildeen.mock.provided.Admin;
import com.kildeen.mock.provided.Pages;
import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import junit.framework.Assert;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
@RunWith(CdiTestRunner.class)
public class PermissionMappingContextTest {

    PermissionMappingContext permissionMappingContext;
    @Inject
    private ViewConfigResolver configResolver;

    @Inject
    private PermissionResolver permissionResolver;

    @Before
    public void setUp() throws Exception {
        permissionMappingContext = new PermissionMappingContext(configResolver.getConfigDescriptors());

    }

    @Test
    public void should_consider_root_view_mappable() throws Exception {
        Assert.assertTrue(permissionMappingContext.isSecuredRoot(configResolver.getConfigDescriptor(Admin.class)));

    }

    @Test
    public void children_should_not_become_root_nodes() throws Exception {
        Assert.assertFalse(permissionMappingContext.isSecuredRoot(configResolver.getConfigDescriptor(Pages.NestedSecured.class)));
        Assert.assertFalse(permissionMappingContext.isSecuredRoot(configResolver.getConfigDescriptor(Pages.NestedSecured.NestedSecuredChild.class)));

    }


    @Test
    public void root_nodes_should_not_have_secured_parents() throws Exception {
        for (Permission rootNode : permissionResolver.getRootPermissions()) {
            if (permissionMappingContext.isSecuredRoot(configResolver.getConfigDescriptor(rootNode.getPath()))) {
                // ok
            } else {
                Assert.fail("A rootnode was detected but it was not a root node");
            }
        }
    }
}
