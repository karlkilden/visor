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

import java.util.HashSet;
import java.util.Set;

/**
 * <p>File created: 2014-02-16 21:35</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public class DuplicatePartPermissionValidator {
    Set<String> usedPermissions = new HashSet<>();


    public void add(String partPermission) throws Exception {
        synchronized (usedPermissions) {
            if (usedPermissions.contains(partPermission)) {
                throw new Exception("Duplicate partPermission detected " + partPermission);
            } else {
                usedPermissions.add(partPermission);
            }
        }
    }
}
