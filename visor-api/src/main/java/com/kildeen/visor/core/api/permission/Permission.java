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

package com.kildeen.visor.core.api.permission;

import java.util.Set;

/**
 * <p>File created: 2014-02-18 20:08</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public interface Permission {

    boolean isParent();

    boolean hasCreate();

    void setCreate(boolean create);
    void pushCreate(boolean create);

    boolean hasRead();

    void setRead(boolean read);
    void pushRead(boolean read);


    boolean hasUpdate();

    void setUpdate(boolean update);
    void pushUpdate(boolean update);

    boolean hasDelete();

    void setDelete(boolean delete);
    void pushDelete(boolean delete);

    void push(SubPermission subPermission, boolean state);

    boolean isPrivileged();

    public Set<Permission> getChildren();

    boolean isGroup();

    int getCount();

    void privilege();

    String getId();

    boolean isCreate();

    boolean isRead();

    boolean isUpdate();

    boolean isDelete();

    String getPath();
}
