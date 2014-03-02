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

package com.kildeen.visor.test.db;

import com.kildeen.visor.core.api.permission.Permission;
import com.kildeen.visor.core.api.permission.PermissionConverter;
import com.kildeen.visor.core.api.permission.PermissionManager;
import com.kildeen.visor.core.api.permission.PermissionResolver;
import com.kildeen.visor.test.Pages;
import com.kildeen.visor.test.model.Group;
import com.kildeen.visor.test.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>File created: 2014-03-01 21:10</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@ApplicationScoped
public class Database {

    @Inject
    private PermissionResolver permissionResolver;

    @Inject
    private PermissionConverter permissionConverter;

    private  List<Group> groups = new ArrayList<>();
    private  List<User> users = new ArrayList<>();
}
