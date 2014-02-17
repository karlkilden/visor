package com.kildeen.visor.core.api.context;

import org.apache.deltaspike.security.api.authorization.SecurityViolation;

class MockSecurityViolation implements SecurityViolation {
    private static final long serialVersionUID = -5017812464381395966L;

    private final String reason;

    MockSecurityViolation(String reason) {
        this.reason = reason;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
