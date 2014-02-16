/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *  * or more contributor license agreements. See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership. The ASF licenses this file
 *  * to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.MockPermissionHolder;
import com.kildeen.visor.core.api.permission.Permission;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 * <p>File created: 2014-02-16 22:04</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@RunWith(CdiTestRunner.class)
public class SubjectPermissionMapperTest {

    @Inject
    MockPermissionHolder permissionHolder;
    @Inject
    SubjectPermissionMapper subjectPermissionMapper;
    @Test
    public void holder_Should_be_mapped_by_string_representation() {
        Permission p = new Permission();
        p.setPermission("tip.tap");
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(p);
        Permission p2 = new Permission();
        p2.setPermission("testTrue");
        permissionSet.add(p2);
        permissionHolder.setPermissions(permissionSet);

        assertEquals(p, subjectPermissionMapper.getPermission("tip.tap"));
        assertFalse(subjectPermissionMapper.hasPermission("testFalse"));
        assertTrue(subjectPermissionMapper.hasPermission("testTrue"));
    }

}
