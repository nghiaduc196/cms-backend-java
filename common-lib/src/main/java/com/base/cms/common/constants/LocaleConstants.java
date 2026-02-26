package com.base.cms.common.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Hằng số locale hỗ trợ đa ngôn ngữ.
 * Có thể mở rộng thêm ngôn ngữ (ja, ko, zh, ...).
 */
public final class LocaleConstants {

    private LocaleConstants() {
    }

    public static final String VI = "vi";
    public static final String EN = "en";

    /** Danh sách locale mặc định hỗ trợ (có thể cấu hình thêm). */
    public static final List<String> SUPPORTED_LOCALES = Arrays.asList(VI, EN);

    public static boolean isSupported(String locale) {
        return locale != null && SUPPORTED_LOCALES.contains(locale.toLowerCase());
    }
}
