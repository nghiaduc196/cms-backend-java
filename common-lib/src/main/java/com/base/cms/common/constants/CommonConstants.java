package com.base.cms.common.constants;

/**
 * Common constants used across microservices
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    /**
     * Email validation pattern (RFC 5322 simplified)
     */
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    /** 0 - Bình thường (normal) */
    public static final String NORMAL = "0";

}
