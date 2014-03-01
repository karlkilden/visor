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

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.security.api.authorization.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class puts ViewConfigDescriptors into "Visor context". This context is required when ViewConfigs are mapped to
 * Permissions thus this context must be created first.
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
final class PermissionMappingContext {

    private static final Logger log = LoggerFactory.getLogger(PermissionResolverImpl.class);

    private Set<String> usedPartPermissionIds = new HashSet<>();
    private Map<Class<?>, ConfigDescriptor> mappedPermissions = new HashMap<>();

    public PermissionMappingContext(List<ConfigDescriptor<?>> configDescriptors) {
        mapPermissions(configDescriptors);

    }

    private void mapPermissions(List<ConfigDescriptor<?>> configDescriptors) {
        for (ConfigDescriptor configDescriptor : configDescriptors) {
           if (isSecured(configDescriptor)) {
               mappedPermissions.put(configDescriptor.getConfigClass(), configDescriptor);
           }
        }
    }

    protected boolean isSecured(ConfigDescriptor<?> configDescriptor) {
        CallbackDescriptor callback = configDescriptor.getCallbackDescriptor(Secured.class);
        return checkIfSecured(callback);
    }


    protected boolean isSecuredRoot(ConfigDescriptor<?> configDescriptor) {
        //Only minimize folders that are secured and lacking secured parent
        return isSecured(configDescriptor)
                && isRoot(configDescriptor.getConfigClass());
    }


    protected boolean isRoot(Class<?> clazz) {
       return !(clazz.getEnclosingClass() != null && isFolder(clazz.getEnclosingClass()) && mappedPermissions.containsKey(clazz));
    }

    protected boolean isFolder(Class<?> configClass) {
        return configClass.isInterface();
    }

    private boolean checkIfSecured(CallbackDescriptor callback) {
        if (callback != null && callback.getCallbackMethods() != null) {
            if (callback.getCallbackMethods().containsKey(PermissionAccessDecisionVoter.class)) {
                return true;
            }
        }
        return false;
    }

    public Map<Class<?>, ConfigDescriptor> getMappedPermissions() {
        return mappedPermissions;
    }

    public void addPermissionIdToUniqueCheckList(String id) throws Exception {
            if (usedPartPermissionIds.contains(id)) {
                throw new Exception("Duplicate partPermission detected " + id);
            } else {
                usedPartPermissionIds.add(id);
            }
    }
}
