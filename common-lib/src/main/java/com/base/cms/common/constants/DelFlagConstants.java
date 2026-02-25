package com.base.cms.common.constants;

/**
 * Hằng số trạng thái del_flag dùng chung (0: bình thường, 1: đã xóa).
 * Dùng khi truy vấn để loại trừ bản ghi đã xóa hoặc lọc theo trạng thái.
 */
public final class DelFlagConstants {

    private DelFlagConstants() {
    }

    /** 0 - Bình thường (normal) */
    public static final String NORMAL = "0";

    /** 1 - Đã xóa (deleted), dùng để loại trừ khi truy vấn */
    public static final String DELETED = "1";
}
