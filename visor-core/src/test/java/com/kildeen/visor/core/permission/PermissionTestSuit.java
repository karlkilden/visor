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

import org.apache.deltaspike.testcontrol.api.junit.CdiTestSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <p>File created: 2014-02-16 22:56</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@RunWith(CdiTestSuiteRunner.class)
@Suite.SuiteClasses({
        PermissionContextTest.class,
        PermissionResolverTest.class
})
public class PermissionTestSuit
{
}

