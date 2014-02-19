package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.Admin;
import com.kildeen.mock.provided.Pages;
import com.kildeen.mock.provided.PermissionMappingContextDummy;
import junit.framework.Assert;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
@RunWith(CdiTestRunner.class)
public class PermissionMappingContextTest {

    PermissionMappingContextDummy permissionMappingContext;
    @Inject
    private ViewConfigResolver configResolver;

    @Before
    public void setUp() throws Exception {
        permissionMappingContext  = new PermissionMappingContextDummy(configResolver.getConfigDescriptors());

    }

    @Test
    public void should_consider_root_view_mappable() throws Exception {
        Assert.assertTrue(permissionMappingContext.shouldMap(        configResolver.getConfigDescriptor(Admin.class)));

    }

    @Test
    public void children_should_not_become_root_nodes() throws Exception {
        Assert.assertFalse(permissionMappingContext.shouldMap(configResolver.getConfigDescriptor(Pages.NestedSecured.class)));
        Assert.assertFalse(permissionMappingContext.shouldMap(configResolver.getConfigDescriptor(Pages.NestedSecured.NestedSecuredChild.class)));

    }
}
