package com.kildeen.mock.provided;

import com.kildeen.visor.core.api.permission.DefaultPermissionRevisionWriter;

import javax.enterprise.inject.Specializes;

/**
 * <p>File created: 2014-02-22 00:28</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@Specializes
public class MockPermissionRevisionWriter extends DefaultPermissionRevisionWriter {

    @Override
    public boolean isActivated() {
        return true;
    }
}
