package com.kildeen.visor.ui;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Created with IntelliJ IDEA.
 * User: Karl Kildén
 * Date: 2014-02-17
 */
public interface Pages extends ViewConfig {
    public class Public1 implements Pages {
    }

    public class Public2 implements Pages {
    }

    public interface Secured extends SecuredPages, ViewConfig {
        public class Secured1 implements Secured {
        }

        public class Secured2 implements Secured {
        }

        public class Secured3 implements Secured {
        }

        public interface Admin extends Secured {

            public class Admin1 implements Secured {
            }

        }
    }
}
