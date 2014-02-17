package com.kildeen.mock.provided;

import com.kildeen.mock.provided.SecuredPages;
import com.kildeen.visor.core.api.context.DefaultAccessDecisionVoter;
import com.kildeen.visor.core.api.permission.PartPermission;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.security.api.authorization.Secured;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
@Secured(value= DefaultAccessDecisionVoter.class)
public interface Pages extends ViewConfig, SecuredPages {
    public class Secured implements Pages {
        public class Part1 implements PartPermission  {}
    }
}
