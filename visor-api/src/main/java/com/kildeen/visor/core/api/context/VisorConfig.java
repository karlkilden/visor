package com.kildeen.visor.core.api.context;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-03-13
 */
public final class VisorConfig {

    public static final class UI {
        public static final class Ajax {
            public static final String SEND_FOR_SELECT = Ajax.class.getSimpleName()+".sendForSelect";
            public static final String SEND_FOR_CHECK = Ajax.class.getSimpleName()+".sendForcheck";
            public static final String SEND_FOR_UNCHECK = Ajax.class.getSimpleName()+".sendForUncheck";

            public static final String ON_SELECT_RENDER = Ajax.class.getSimpleName()+".onSelectRender";
            public static final String ON_CHECK_RENDER = Ajax.class.getSimpleName()+".onCheckRender";
            public static final String ON_UNCHECK_RENDER = Ajax.class.getSimpleName()+".onUncheckRender";
        }

        public static final class Appearance {
            public static final String NODE_LABEL = Appearance.class.getSimpleName()+".nodeLabel";
            public static final String EXPAND_SYMBOL = Appearance.class.getSimpleName()+".expandSymbol";
            public static final String COLLAPSE_SYMBOL = Appearance.class.getSimpleName()+".collapseSymbol";
            public static final String EXPAND_SYMBOL_MODE = Appearance.class.getSimpleName()+".expandSymbolMode";
            public static final String COLLAPSE_SYMBOL_MODE = Appearance.class.getSimpleName()+".collapseSymbolMode";

            public static final String DISPLAY_NODE_SELECT_INHERITANCE_CONTROL = Appearance.class.getSimpleName()+".displayNodeSelectInheritanceControl";
            public static final String DISPLAY_NODE_CLICK_MODE_CONTROL = Appearance.class.getSimpleName()+".displayNodeClickMode";
        }

        public static final class Behavior {
            public static final String NODE_CLICK_MODE = Behavior.class.getSimpleName()+".nodeClickMode";
            public static final String NODE_SELECT_INHERITANCE = Behavior.class.getSimpleName()+".nodeSelectInheritance";
        }
    }
}