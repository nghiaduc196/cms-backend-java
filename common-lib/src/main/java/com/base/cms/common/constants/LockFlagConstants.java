package com.base.cms.common.constants;

/**
 * Hằng số trạng thái lock_flag dùng chung (0: bình thường/hoạt động, 9: khóa).
 */
public final class LockFlagConstants {

    private LockFlagConstants() {
    }

    /** 0 - Bình thường / đang hoạt động (normal) */
    public static final String NORMAL = "0";

    /** 9 - Đã khóa (locked), không hoạt động */
    public static final String LOCKED = "9";
}
