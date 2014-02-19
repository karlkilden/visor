package com.kildeen.visor.core.permission;

import com.kildeen.visor.core.api.permission.PermissionResolver;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * <p>File created: 2014-02-17 20:56</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@RunWith(CdiTestRunner.class)
public class PermissionModelTest {

    @Inject
    private PermissionResolver resolver;

    @Test
    public void permissionFolder_should_have_correct_name() throws Exception {

        Assert.assertEquals("kildeen.mock.provided.Pages", resolver.getRootPermissionModels().get(0).getId());
    }
}
