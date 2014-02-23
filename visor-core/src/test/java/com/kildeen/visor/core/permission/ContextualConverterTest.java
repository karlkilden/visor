package com.kildeen.visor.core.permission;

import com.kildeen.mock.provided.MockPermissionHolder;
import com.kildeen.visor.core.api.permission.*;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
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
    private MockPermissionHolder permissionHolder;

    @Inject
    private PermissionMinimizer builder;


    @Test
    public void minimize_should_create_one_to_one() throws Exception {
        PermissionImpl p = new PermissionImpl("kildeen.mock.provided.Pages.NestedSecured.NestedSecuredChild", new ListOrderedSet<Permission>(),null);

        p.setCreate(true);
        p.setRead(true);
        p.setUpdate(true);
        p.setDelete(true);
        PermissionImpl child = new PermissionImpl("kildeen.mock.provided.Pages.Secured",new ListOrderedSet<Permission>(),null);

        List<Permission> list = new ArrayList<>();
        list.add(child);
        list.add(p);
        MinimizedPermission truncated =  builder.minimize(list);
        assertEquals(2, truncated.getTruncatedPermissionModel().size());
    }

    @Test
    public void minmize_should_be_serialize_and_deserialize() throws Exception {
        PermissionImpl p = new PermissionImpl("kildeen.mock.provided.Pages.NestedSecured.NestedSecuredChild", new ListOrderedSet<Permission>(),null);

        p.setCreate(true);
        p.setRead(true);
        p.setUpdate(true);
        p.setDelete(false);
        PermissionImpl child = new PermissionImpl("kildeen.mock.provided.Pages.Secured",new ListOrderedSet<Permission>(),null);

        List<Permission> list = new ArrayList<>();
        list.add(child);
        list.add(p);
        String s = permissionConverter.minimize(list);

        Collection<Permission> back = permissionConverter.expand(s);
        for (Permission permission : back) {
            assertTrue(list.contains(permission));
            if (p.equals(permission)) {
                assertTrue(permission.isDelete() == false);
            }
        }

        permissionConverter.automaticSerializeAll(list);
    }
}
