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

import com.kildeen.visor.core.api.permission.DefaultPermissionConverter;
import com.kildeen.visor.core.api.permission.Permission;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * <p>File created: 2014-02-16 01:41</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class ConverterTest {

    @Test
    public void default_converter_should_remove_first_package_folder() {
         DefaultPermissionConverter converter = new DefaultPermissionConverter();

        String expected =   "kildeen.visor.core.permission.ConverterTest";
        assertEquals(expected, converter.getId(this.getClass()));
    }



    @Test
    public void serialized_should_be_as_was_when_deserialized() {
        DefaultPermissionConverter converter = new DefaultPermissionConverter();

        PermissionImpl p = new PermissionImpl("this.that", new ListOrderedSet<Permission>(),null);

        p.setCreate(true);
        p.setRead(true);
        p.setUpdate(true);
        p.setDelete(true);
        PermissionImpl child = new PermissionImpl("this.that.do",new ListOrderedSet<Permission>(),null);
        p.getChildren().add(child);

        String json = converter.serialize(p);
        Permission p2 = converter.deserialize(json);

        assertEquals(p.hasCreate(), p2.hasCreate());
        assertEquals(p.hasRead(), p2.hasRead());
        assertEquals(p.getChildren().iterator().next().getId(), p2.getChildren().iterator().next().getId());



    }
}
