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

package com.kildeen.visor.core.api.permission;

import java.io.Serializable;
import java.util.Collection;

/**
 * Permissions are string based. The conversion between the
 * {@link org.apache.deltaspike.core.api.config.view.ViewConfig}
 * config class
 *  is centralized to this converter. To extend or change behavior use {@link javax.enterprise.inject.Specializes} and
 *  override {@link DefaultPermissionConverter}.
 *
 * <p>File created: 2014-02-16 01:35</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
public interface PermissionConverter extends Serializable {

    /**
     *
     * @param permissionClass  The current view config class that should be converted.
     * @return The result of the conversion. Default will use #getCanonicalName() and then strip the first "folder".
     * The intention is to dropout words such as "com", "org" since they offer no value.
     */
    public String getPermissionId(Class<?> permissionClass);

    /**
     * A PartPermission is a nested class nested inside a ViewConfig that implements {@link PartPermission}. Default
     * Conversion for a PartPermission is uncapitalized #getSimpleName() from parent + #getSimpleName().
     * For example if the class is defined as com.mypackage.Admin.Part1 it will be converted to "adminPart1". The
     * converter will not detect naming collisions. However {@link PermissionResolver} will if used.
     * @param partPermissionClass  The partPermission that should be converted
     * @return conversion result
     */
    public String getPartPermissionId(Class<? extends PartPermission> partPermissionClass);

    /**
     *
     * @param permissionFolderClass class to Convert.
     * @return  Default implementation is exact same as {@link #getPermissionId(Class)}
     */
    public String getPermissionGroupId(Class<?> permissionFolderClass);


    /**
     *
     * @param permission  Permission that should be converted to a String to simplify persisting etc.
     * @return  Default turns it into a default json string object.
     */
    public String serialize(PermissionModel permission);

    /**
     *
     * @param deserializedPermission  A String constructed by using {@link #serialize(PermissionModel)}
     * @return  A permission as was before {@link #serialize(PermissionModel)} was used.
     */
    public Permission deserialize(String deserializedPermission);

    /**
     *
     * @param permissions to serialize.
     * @return Convenience method, Default uses {@link #serialize(PermissionModel)} for all entries.
     */
    public Collection<String> serializeAll(Collection<Permission> permissions);

    /**
     *
     * @param deserializedPermissions to deserialize.
     * @return  Convenience method, Default uses {@link #deserialize(String)}} for all entries.
     */
    public Collection<Permission> deserializeAll(Collection<String> deserializedPermissions);


}
