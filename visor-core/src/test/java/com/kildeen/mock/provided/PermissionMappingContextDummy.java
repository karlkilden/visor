package com.kildeen.mock.provided;

import com.kildeen.visor.core.permission.PermissionMappingContext;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
public class PermissionMappingContextDummy extends PermissionMappingContext {
    public PermissionMappingContextDummy(List<ConfigDescriptor<?>> configDescriptors) {
        super(configDescriptors);
    }
}
