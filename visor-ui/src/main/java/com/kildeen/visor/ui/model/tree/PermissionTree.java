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

package com.kildeen.visor.ui.model.tree;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.event.ActionEvent;

/**
 * <p>File created: 2014-03-17 20:43</p>
 *
 * @version 1.0
 * @author: Karl Kildén
 * @since 1.0
 */
@FacesComponent("com.kildeen.visor.tree")
public class PermissionTree extends UINamingContainer {

    enum PropertyKeys {
        collapsed, onSelectRender, onCheckRender, onUncheckRender
    }

    public boolean isCollapsed() {
        return (Boolean) getStateHelper().eval(
                PropertyKeys.collapsed, Boolean.FALSE);
    }


    public void setCollapsed(boolean collapsed) {
        getStateHelper().put(PropertyKeys.collapsed, collapsed);
    }

    public void toggle(ActionEvent e) {
        setCollapsed(!isCollapsed());
    }

    public String getOnSelectRender() {
        return (String) getStateHelper().eval(
                PropertyKeys.onSelectRender);
    }

    public String getOnCheckRender() {
        return (String) getStateHelper().eval(
                PropertyKeys.onCheckRender);    }

    public String getOnUncheckRender() {
        return (String) getStateHelper().eval(
                PropertyKeys.onUncheckRender);    }

    public void setOnSelectRender(final String onSelectRender) {
        getStateHelper().put(PropertyKeys.onSelectRender, onSelectRender);
    }

    public void setOnCheckRender(final String onCheckRender) {
        getStateHelper().put(PropertyKeys.onCheckRender, onCheckRender);
    }

    public void setOnUncheckRender(final String onUncheckRender) {
        getStateHelper().put(PropertyKeys.onUncheckRender, onUncheckRender);
    }
}