package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.MockPermissionHolder;
import com.kildeen.visor.core.api.permission.*;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-19
 */
@RunWith(CdiTestRunner.class)
public class ContextualConverterTest {
    @Inject
    private PermissionConverter permissionConverter;

    @Inject
    MockPermissionHolder permissionHolder;

    @Inject
    TruncatedPermissionBuilder builder;


    @Test
    public void truncate_should_create_one_to_one() throws Exception {
        Permission p = new Permission("kildeen.mock.provided.Pages.NestedSecured.NestedSecuredChild", new ListOrderedSet<PermissionModel>(),null);

        p.setCreate(true);
        p.setRead(true);
        p.setUpdate(true);
        p.setDelete(true);
        Permission child = new Permission("kildeen.mock.provided.Pages.Secured",new ListOrderedSet<PermissionModel>(),null);

        List<PermissionModel> list = new ArrayList<>();
        list.add(child);
        list.add(p);
        TruncatedPermissionModel truncated =  builder.map(list);
        assertEquals(2, truncated.getTruncatedPermissionModel().size());
        List<PermissionModel> expanded = permissionConverter.expand(truncated.getTruncatedPermissionModel());
         assertTrue(expanded.contains(p));
        assertTrue(expanded.contains(child));


    }
}
