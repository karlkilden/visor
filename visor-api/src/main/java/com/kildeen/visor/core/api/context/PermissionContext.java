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

package com.kildeen.visor.core.api.context;

import com.kildeen.visor.core.api.permission.Permission;

import java.io.Serializable;

/**
 * The current PermissionContext. It contains state for the current request and is the main
 * interaction point with the permission flow that visor offers. It is also used internally by all security mechanisms.
 * <p/>
 * <p>File created: 2014-02-15 13:51</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */

public interface PermissionContext extends Serializable {
    /**
     * The current view is considered to be secured if {@link org.apache.deltaspike.security.api.authorization.Secured}
     * is part of it's viewMetaData as per the definition from DeltaSpike.
     *
     * @return true of the current view is secured, false otherwise.
     */
    public boolean isSecured();

    public boolean isAllowed();

    public boolean isCreate();

    public boolean isRead();

    public boolean isReadOnly();

    public boolean isUpdate();

    public boolean isDelete();

    public boolean has(String id);

    public boolean hasCreate(String partPermission);

    public boolean hasRead(String partPermission);

    public boolean hasUpdate(String partPermission);

    public boolean hasDelete(String partPermission);

    public Permission getRequired();
}
