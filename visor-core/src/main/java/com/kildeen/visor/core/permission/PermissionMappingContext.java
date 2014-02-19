package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.context.PermissionAccessDecisionVoter;
import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.security.api.authorization.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class PermissionMappingContext {

    private static final Logger log = LoggerFactory.getLogger(PermissionResolverImpl.class);

    private Set<String> usedPartPermissionIds = new HashSet<>();
    private Map<Class<?>, ConfigDescriptor> mappedPermissionModels = new HashMap<>();

    public PermissionMappingContext(List<ConfigDescriptor<?>> configDescriptors) {
        mapPermissionModels(configDescriptors);

    }

    private void mapPermissionModels(List<ConfigDescriptor<?>> configDescriptors) {
        for (ConfigDescriptor configDescriptor : configDescriptors) {
           if (isSecured(configDescriptor)) {
               mappedPermissionModels.put(configDescriptor.getConfigClass(), configDescriptor);
           }
        }
    }

    protected boolean isSecured(ConfigDescriptor<?> configDescriptor) {
        CallbackDescriptor callback = configDescriptor.getCallbackDescriptor(Secured.class);
        return checkIfSecured(callback);
    }


    protected boolean isSecuredRoot(ConfigDescriptor<?> configDescriptor) {
        //Only map folders that are secured and lacking secured parent
        return isSecured(configDescriptor)
                && isRoot(configDescriptor.getConfigClass());
    }


    protected boolean isRoot(Class<?> clazz) {
       return !(clazz.getEnclosingClass() != null && isFolder(clazz.getEnclosingClass()) && mappedPermissionModels.containsKey(clazz));
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

    public Map<Class<?>, ConfigDescriptor> getMappedPermissionModels() {
        return mappedPermissionModels;
    }

    public void addPermissionIdToUniqueCheckList(String id) throws Exception {
        synchronized (usedPartPermissionIds) {
            if (usedPartPermissionIds.contains(id)) {
                throw new Exception("Duplicate partPermission detected " + id);
            } else {
                usedPartPermissionIds.add(id);
            }
        }
    }
}
